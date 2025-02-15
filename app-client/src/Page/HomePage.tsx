import {isPagePath, NavPathResult, NavTransition} from "Design/NavigationProvider";
import React, {SyntheticEvent} from "react";
import {ContainerCard} from "Design/ContainerCard";
import {LargeContentMain} from "Design/LayoutMain";
import {DateDisplay, raidoTitle, RoleDisplay} from "Component/Util";
import {
  Alert,
  IconButton,
  Menu,
  MenuItem,
  Snackbar,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow
} from "@mui/material";
import {useAuthApi} from "Api/AuthApi";
import {useQuery} from "@tanstack/react-query";
import {CompactErrorPanel} from "Error/CompactErrorPanel";
import {TextSpan} from "Component/TextSpan";
import {useAuth} from "Auth/AuthProvider";
import {RqQuery} from "Util/ReactQueryUtil";
import {RaidListItemV2} from "Generated/Raidv2";
import {InfoField, InfoFieldList} from "Component/InfoField";
import {RefreshIconButton} from "Component/RefreshIconButton";
import {CompactLinearProgress} from "Component/SmallPageSpinner";
import {RaidoLink} from "Component/RaidoLink";
import {RaidoAddFab} from "Component/AppButton";
import {getEditRaidPageLink} from "Page/EditRaidPage";
import {getMintRaidPageLink} from "Page/MintRaidPage";
import {IdProviderDisplay} from "Component/IdProviderDisplay";
import {ContentCopy, FileDownload, Settings} from "@mui/icons-material";
import {toastDuration} from "Design/RaidoTheme";
import {assert} from "Util/TypeUtil";
import Typography from "@mui/material/Typography";
import {formatLocalDateAsFileSafeIsoShortDateTime, formatLocalDateAsIso} from "Util/DateUtil";
import {escapeCsvField} from "Util/DownloadUtil";

const log = console;

const pageUrl = "/home";



export function getHomePageLink(): string{
  return pageUrl;
}

export function isHomePagePath(pathname: string): NavPathResult{
  const pathResult = isPagePath(pathname, pageUrl);
  if( pathResult.isPath ){
    return pathResult;
  }

  // use this page as the "default" or "home" page for the app  
  if( pathname === "" || pathname === "/" ){
    return {isPath: true, pathSuffix: ""};
  }

  /* Temporary workaround - the legacy app used land on a page like this,
   if we didn't catch it, user would get a blank page.
   This could be a problem actually if they do this with a bunch of urls, the
   current page routing mechanism has no "catch all" mechanism (and admitted
   short-coming). */
  if( pathname === "/login.html" ){
    return {isPath: true, pathSuffix: ""};
  }
  
  return { isPath: false }
}

export function HomePage(){
  return <NavTransition isPagePath={isHomePagePath} 
    title={raidoTitle("Home")}>
    <Content/>
  </NavTransition>
}


function Content(){
  const {session: {payload: user}} = useAuth();
  return <LargeContentMain>
    <RaidCurrentUser/>
    <br/>
    <RaidTableContainerV2 servicePointId={user.servicePointId}/>
  </LargeContentMain>
}

function RaidCurrentUser(){
  const api = useAuthApi();
  const {session: {payload: user}} = useAuth();
  const spQuery = useQuery(['readServicePoint', user.servicePointId],
    async () => await api.admin.readServicePoint({
      servicePointId: user.servicePointId }));
  return <ContainerCard title={"Signed-in user"}>
    <InfoFieldList>
      <InfoField id={"email"} label={"Identity"} value={user.email}/>
      <InfoField id={"idProvider"} label={"ID provider"}
        value={<IdProviderDisplay payload={user}/> }/>
      <InfoField id={"servicePoint"} label={"Service point"} 
        value={spQuery.data?.name || ""}
      />
      <InfoField id={"role"} label={"Role"} value={
        <RoleDisplay role={user.role}/>
      }/>
    </InfoFieldList>
    <CompactErrorPanel error={spQuery.error}/>
  </ContainerCard>
  
}


export function RaidTableContainerV2({servicePointId}: {servicePointId: number}){
  const [handleCopied, setHandleCopied] = React.useState(
    undefined as undefined | string);

  const api = useAuthApi();
  const {session: {payload: user}} = useAuth();

  const raidQuery: RqQuery<RaidListItemV2[]> =
    useQuery(['listRaids', servicePointId], async () => {
      return await api.basicRaid.listRaidV3({
        raidListRequestV2: {servicePointId: servicePointId}
      });
    });
    
  const spQuery = useQuery(['readServicePoint', user.servicePointId],
    async () => await api.admin.readServicePoint({
      servicePointId: user.servicePointId }));

  const appWritesEnabled = spQuery.data?.appWritesEnabled;

  if( raidQuery.error ){
    return <CompactErrorPanel error={raidQuery.error}/>
  }

  const onCopyHandleClicked = async (e: SyntheticEvent)=>{
    const handle = e.currentTarget.getAttribute("data-handle");
    assert(handle, "onCopyHandleClicked() called with no handle"); 
    await navigator.clipboard.writeText(handle);
    setHandleCopied(handle);
  };

  const handleToastClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
    // source copied from https://mui.com/material-ui/react-snackbar/#simple-snackbars
    if (reason === 'clickaway') {
      return;
    }

    setHandleCopied(undefined);
  };
  
  return<>
    {/* Ensure the `readServicePoint` data has completely loaded before evaluating `spQuery`.
        This prevents a flash of the warning message when the page first loads.
    */}
    { !appWritesEnabled && !spQuery.isLoading ? <Alert severity="warning">Editing is disabled for this service point.</Alert> : <></> }
  
  <ContainerCard title={"Recently minted RAiD data"}
    action={<>
      <SettingsMenu raidData={raidQuery.data} />
      <RefreshIconButton onClick={() => raidQuery.refetch()}
        refreshing={raidQuery.isLoading || raidQuery.isRefetching} />
       <RaidoAddFab disabled={!appWritesEnabled} href={getMintRaidPageLink(servicePointId)}/>
    </>}
  >
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Primary title</TableCell>
            <TableCell>Handle</TableCell>
            <TableCell>Start date</TableCell>
            <TableCell>Create date</TableCell>
          </TableRow>
        </TableHead>
        { raidQuery.isLoading &&
          <TableBody><TableRow style={{border: 0}}>
            <TableCell colSpan={10} style={{border: 0, padding: 0}}>
              <CompactLinearProgress isLoading={true}/>
            </TableCell>
          </TableRow></TableBody>
        }
        { !raidQuery.isLoading && raidQuery.data?.length === 0 &&
          <TableBody><TableRow style={{border: 0}}>
            <TableCell colSpan={10} 
              style={{border: 0, padding: 0, textAlign: "center"}} 
            >
              <TextSpan style={{lineHeight: "3em"}}>
                No RAiD data has been minted yet.
              </TextSpan>
            </TableCell>
          </TableRow></TableBody>
        }
        <TableBody>
          { raidQuery.data?.map((row) => (
            <TableRow
              key={row.handle}
              data-handle={row.handle}
              // don't render a border under last row
              sx={{'&:last-child td, &:last-child th': {border: 0}}}
            >
              <TableCell
                style={{
                  /* without this, long titles can push the other columns over 
                  to the right and the table scrolls.
                  I can't figure out how to limit column width to percentage. */
                  maxWidth: "40em",
                  /* if overflow is not hidden, then really long unbreakable 
                   strings (like those generated by int tests with no 
                   whitespace or punctuation) are rendered over the top of the
                   next column. */
                  overflow: "hidden",
                  /* This makes anything that does get clipped by overflow 
                  hidden will have ellipsis to indicate the clipping. */
                  textOverflow: "ellipsis",
                }}
              >
                <RaidoLink href={getEditRaidPageLink(row.handle)}>
                  <TextSpan>{row.primaryTitle || ''}</TextSpan>
                </RaidoLink>
              </TableCell>
              <TableCell>
                <RaidoHandle handle={row.handle} 
                  onCopyHandleClicked={onCopyHandleClicked} />
              </TableCell>
              <TableCell>
                <DateDisplay date={row.startDate}/>
              </TableCell>
              <TableCell>
                <DateDisplay date={row.createDate}/>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

    </TableContainer>
    {/* This is first time I've used the Snackbar.
    Personally I don't like toasts most of the time, but the copy button has
    no feedback, so I felt it was necessary.
    There's a lot of improvements to be made here.
    I'd rather the snackbar was global, and we kept a history of all these 
    notifications (just in memory, for the life of the browsing context, not
    local storage or anything like that. */}
    <Snackbar open={!!handleCopied} autoHideDuration={toastDuration} 
      onClose={handleToastClose}
    >
      <Alert onClose={handleToastClose} severity="info" sx={{ width: '100%' }}>
        Handle {handleCopied} copied to clipboard.
      </Alert>
    </Snackbar>
  </ContainerCard>
  </>
}

function RaidoHandle({handle, onCopyHandleClicked}:{
  handle: string, 
  onCopyHandleClicked: (event: SyntheticEvent)=>void,
}){
  return <TextSpan noWrap={true}>
    {handle || ''}
    {' '}
    {/* using dataset because creating a list of 500 items would create 500  
    onClick handlers, but stringly-typing like this is 🤮
    I honestly don't know which is worse.  Is 500 onClick handlers even worth
    worrying about? */}
    <IconButton color={"primary"} data-handle={handle}
      onClick={onCopyHandleClicked}
    >
      <ContentCopy/>
    </IconButton>
  </TextSpan>
}

function SettingsMenu({raidData}:{
  raidData: RaidListItemV2[]|undefined
}){
  const[ isMenuOpen, setIsMenuOpen] = React.useState(false);
  const menuAnchorRef = React.useRef<HTMLButtonElement>(null!);

  function onClose(){
    setIsMenuOpen(false);
  }

  // taken from https://stackoverflow.com/a/40657767/924597
  function downloadData(){
    
    assert(raidData, "raid data was empty when download clicked");

    const escapedTextData = raidData.map(iRaid => {
      return [
        escapeCsvField(iRaid.primaryTitle),
        escapeCsvField(iRaid.handle),
        escapeCsvField(formatLocalDateAsIso(iRaid.startDate)),
        escapeCsvField(formatLocalDateAsIso(iRaid.createDate)),
      ]
    });
    escapedTextData.unshift([
      "Primary title", "Handle", "Start date", "Create date" ]);

    const csvData = escapedTextData.
      map(iRow => iRow.join(",")).
      join("\n");
    
    const downloadLink = "data:text/csv;charset=utf-8," + csvData;

    /* I wanted to control the filename, so took from: 
     https://stackoverflow.com/a/50540808/924597 */
    const link = document.createElement("a");
    link.href = downloadLink;
    const fileSafeTimestamp = 
      formatLocalDateAsFileSafeIsoShortDateTime(new Date());
    link.download = `recent-raids-${fileSafeTimestamp}.csv`;
    link.click();    
  }

  const noRaidData = !raidData || raidData.length === 0;

  return <>
    <IconButton 
      ref={menuAnchorRef}
      onClick={()=> setIsMenuOpen(true)}
      color="primary"
    >
      <Settings/>
    </IconButton>

    <Menu id="menu-homepage-settings"
      anchorEl={menuAnchorRef.current}
      open={isMenuOpen}
      onClose={()=> setIsMenuOpen(false)}
    >
      <MenuItem disabled={noRaidData} 
        onClick={()=>{
          downloadData();
          onClose();
        }}
      >
        <Typography>
          <FileDownload style={{verticalAlign: "bottom"}}/>
          Download report of recently minted RAiDs
        </Typography>
      </MenuItem>
    </Menu>
  </>;
}
