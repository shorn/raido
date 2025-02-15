import { useNavigation } from "Design/NavigationProvider";
import React, { ReactNode, useState } from "react";
import {
  AppBar,
  Hidden,
  IconButton,
  ListItemIcon,
  Menu,
  MenuItem,
  Toolbar
} from "@mui/material";
import { AccountCircle, Logout, Menu as MenuIcon } from "@mui/icons-material";
import Typography from "@mui/material/Typography";
import { AppDrawer } from "Design/AppDrawer";
import { RaidoLogoSvg } from "Component/Icon";
import { getHomePageLink } from "Page/HomePage";
import { Color } from "Design/RaidoTheme";
import { useAuth } from "Auth/AuthProvider";
import { IdProviderDisplay } from "Component/IdProviderDisplay";
import { Config } from "Config";
import { formatShortTime } from "Util/DateUtil";
import { InfoField } from "Component/InfoField";

const log = console;

export function AppNavBar(){
  const nav = useNavigation();
  

  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <AppBar position="static">
      <EnvironmentBanner />
      <Toolbar variant={"dense"}>
        <IconButton
          color="inherit"
          href={getHomePageLink()}
          onClick={event=>nav.navigateTo(getHomePageLink(), event)}
          size="small"
          // to make the nav bar not so tall
          style={{padding: 0, maxHeight: "2em"}}
        >
          <RaidoLogoSvg
            // improve:sto should use theme color, not like this
            //color={'theme.primary'}
            color={Color.lotion}
            
            /* to make the nav bar not so tall, along with the style at the
            IconButton level.  I don't know why the Raido svg causes it to be
            so tall. */
            style={{scale: '50%'}}
          />
        </IconButton>
        <MenuShortcutBar>
          <MenuShortcutNavItem href={getHomePageLink()}>
            Home
          </MenuShortcutNavItem>
        </MenuShortcutBar>

        {/* flexgrow pushes the icons over to the right */}
        <Typography variant="h6" color="inherit" style={{flexGrow: 1}} />
        <div>
          <AccountMenu/>
          <IconButton color="inherit" onClick={()=>setDrawerOpen(true)} size="large">
            <MenuIcon/>
          </IconButton>
          <AppDrawer anchor={"right"} open={drawerOpen}
            toggleDrawer={setDrawerOpen} />
        </div>

      </Toolbar>
    </AppBar>
  );
}

function MenuShortcutBar(props:{children: React.ReactNode}){
  return (
    <Hidden mdDown>
      <span style={{
        // Avoid shortcuts wrapping which causes AppBar to grow in height
        display: "flex", flexWrap: "nowrap", overflow: "hidden"
      }}>
        {props.children}
      </span>
    </Hidden>
  );
}

function MenuShortcutNavItem(props: {
  children: React.ReactNode,
  href: string,
}){
  const nav = useNavigation();
  return (
    <IconButton
      color="inherit"
      href={props.href}
      onClick={event=>nav.navigateTo(props.href, event)}
      size="small" 
    >
      {props.children}
    </IconButton>
  );
}

function AccountMenu(){
  const auth = useAuth();
  const[ isMenuOpen, setIsMenuOpen] = React.useState(false);
  const menuAnchorRef = React.useRef<HTMLButtonElement>(null!);
  const nav = useNavigation();
  const session = auth.session;

  function onClose(){
    setIsMenuOpen(false);
  }

  return <>
    <IconButton
      ref={menuAnchorRef}
      onClick={()=> setIsMenuOpen(true)}
      style={{paddingRight: 20}}
      color="inherit"
      size="large">
      <AccountCircle/>
    </IconButton>

    <Menu id="menu-appbar"
      anchorEl={menuAnchorRef.current}
      anchorOrigin={{vertical: 'top', horizontal: 'right'}}
      transformOrigin={{vertical: 'top', horizontal: 'right'}}
      open={isMenuOpen}
      onClose={()=> setIsMenuOpen(false)}
    >
      <InfoMenuItem>
        <div style={{display: "grid", gridTemplateColumns: "1fr 1fr"}}>
          <InfoField label="Identity"
            value={session.payload.email}
          />
          <InfoField label={"ID provider"}
            value={<IdProviderDisplay payload={session.payload}/> }/>
          <InfoField label="Signed in"
            value={formatShortTime(session.accessTokenIssuedAt)}
          />
          <InfoField label="Session expiry"
            value={formatShortTime(session.accessTokenExpiry)}
          />
        </div>
      </InfoMenuItem>
      <MenuItem onClick={async ()=>{
        log.debug("clicked sign-out");
        auth.signOut();
      }}>
        <ListItemIcon>
          <Logout fontSize="small" />
        </ListItemIcon>
        <Typography>Sign out</Typography>
      </MenuItem>
    </Menu>
  </>;
}

export function InfoMenuItem({children}: {
  children: ReactNode,
}){
  return <MenuItem disabled={true} sx={{
    '&.Mui-disabled': {
      opacity: 1
    },
  }} >
      {children}
  </MenuItem>
}

export function EnvironmentBanner(){
  if( Config.isProd ){
    return null;
  }
  
  return <aside className="raido-environment-banner">
    {Config.environmentName.toUpperCase()}
  </aside>
}