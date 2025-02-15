import React from "react";
import { isPagePath } from "Design/NavigationProvider";
import { normalisePath } from "Util/Location";
import {
  AlternateUrlBlock,
  PublicClosedMetadataSchemaV1,
  PublicRaidMetadataSchemaV1,
  PublicReadRaidResponseV3
} from "Generated/Raidv2";
import { Config } from "Config";
import { RqQuery } from "Util/ReactQueryUtil";
import {
  QueryClient,
  QueryClientProvider,
  useQuery
} from "@tanstack/react-query";
import { publicApi } from "Api/SimpleApi";
import {
  SmallContentMain,
  SmallScreenMain,
  SmallScreenPaper
} from "Design/LayoutMain";
import { InfoField, InfoFieldList } from "Component/InfoField";
import { SmallPageSpinner } from "Component/SmallPageSpinner";
import { CompactErrorPanel } from "Error/CompactErrorPanel";
import { BooleanDisplay, DateDisplay } from "Component/Util";
import { TextSpan } from "Component/TextSpan";
import { formatMetadata, getPrimaryTitle } from "Component/MetaDataContainer";
import { List, ListItem } from "@mui/material";
import { NewWindowLink } from "Component/ExternalLink";

const pageUrl = "/handle";

export function formatCnriUrl(handle: string){
  return `https://hdl.handle.net/${handle}`;
}

/*
Not used by anything in the app, the "public landing page" link from the raid 
edit page links via the https://hdl.handle.net domain, which redirects to this
url, but that's encoding the raid url (content path) on the server.

*/
export function getRaidLandingPagePath(handle: string): string{
  return `${Config.raidoLandingPage}/${handle}`;
}

export function isRaidLandingPagePath(pathname: string): boolean{
  return normalisePath(pathname).startsWith(pageUrl);
}

export function RaidLandingPage(){
  const isPath = isPagePath(window.location.pathname, pageUrl);

  if( !isPath.isPath ){
    return null;
  }

  if( !isPath.pathSuffix ){
    throw new Error("could not parse handle from path");
  }

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        ...Config.publicApiQuery,
      }
    }
  });

  return <>
    <QueryClientProvider client={queryClient}>
      <Content handle={isPath.pathSuffix}/>
    </QueryClientProvider>
  </>;
}

function Content({handle}: {handle: string}){
  const api = publicApi();
  const queryName = 'publicReadRaid';
  const query: RqQuery<PublicReadRaidResponseV3> = useQuery(
    [queryName, handle],
    async () => {
      //await delay(2000);
      return await api.publicReadRaidV3({handle});
    }
  );

  if( query.isLoading ){
    return <SmallPageSpinner message={"loading RAiD data"}/>
  }

  if( query.error ){
    return <CompactErrorPanel error={query.error}/>
  }

  if( !query.data ){
    return <CompactErrorPanel
      error={"unknown state: not loading, no error, no data"}/>
  }

  if( !query.data.metadata ){
    return <CompactErrorPanel
      error={"no metadata in response"}/>
  }

  // just in case things go weird
  console.debug("landingPage.render()", typeof query.data, query.data);

  // now typesafe with the discriminator - yay openapi!
  const metadata = query.data.metadata;
  if( metadata.metadataSchema === "PublicRaidMetadataSchemaV1" ){
    return <OpenRaid raid={query.data} metadata={metadata}/>
  }
  else if( metadata.metadataSchema === "PublicClosedMetadataSchemaV1" ){
    return <ClosedRaid raid={query.data} metadata={metadata}/>
  }
  else {
    return <CompactErrorPanel error={{
      message: `unknown metadataSchema`,
      problem: query.data
    }}/>
  }
}

function ClosedRaid({raid, metadata}: {
  raid: PublicReadRaidResponseV3,
  metadata: PublicClosedMetadataSchemaV1,
}){
  return <SmallContentMain><InfoFieldList>
    <InfoField id="globalHandle" label="CNRI URL"
      value={formatCnriUrl(raid.handle)}/>
    <InfoField id="createDate" label="Create date" value={
      <DateDisplay date={raid.createDate}/>
    }/>

    <InfoField id="confidential" label="Confidential"
      value={<BooleanDisplay value={true}/>}
    />
    
    <InfoField id="confidential" label="Access statement"
      value={<TextSpan>{metadata.access.accessStatement}</TextSpan>}
    />
  </InfoFieldList></SmallContentMain>
}

function OpenRaid({raid, metadata}: {
  raid: PublicReadRaidResponseV3,
  metadata: PublicRaidMetadataSchemaV1,
}){
  return <SmallScreenMain>
    <SmallScreenPaper>  
      <InfoFieldList>
        <InfoField id="globalUrl" label="CNRI URL"
          value={formatCnriUrl(raid.handle)}/>
        <InfoField id="createDate" label="Create date" value={
          <DateDisplay date={raid.createDate}/>
        }/>

        <InfoField id="confidential" label="Confidential"
          value={<BooleanDisplay value={false}/>}
        />

        <InfoField id="servicePoint" label="Service point"
          value={raid.servicePointName}
        />

        <InfoField id="primaryTitle" label="Primary title"
          value={getPrimaryTitle(metadata).title}/>

        <InfoField id="startDate" label="Start date" value={
          <DateDisplay date={metadata.dates.startDate}/>
        }/>
      </InfoFieldList>
    </SmallScreenPaper>
    <AlternateUrlLinks urls={metadata.alternateUrls}></AlternateUrlLinks>
    <SmallScreenPaper>
      <pre style={{overflowX: "scroll"}}>
        {formatMetadata(metadata)}
      </pre>
    </SmallScreenPaper>
  </SmallScreenMain>
}

function AlternateUrlLinks({urls}:{urls?: AlternateUrlBlock[]}){
  if( !urls || urls.length === 0 ){
    return null;
  }
  
  return <SmallScreenPaper>
    <List>{urls.map(iUrl=>
      <ListItem key={iUrl.url}>
        <NewWindowLink href={iUrl.url}>
          {iUrl.url}
        </NewWindowLink>
      </ListItem>
    )}</List>
  </SmallScreenPaper>
}