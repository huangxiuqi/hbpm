/// <reference types="vite/client" />
declare namespace PageVars {
  type ClientInfo = {
    name: string;
    desc: string;
    icon: string;
  }

  type LoginPageVars = {
    clientInfo: ClientInfo;
  }
}


