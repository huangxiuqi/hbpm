namespace API {
  interface GatewayRouteDefine {
    id?: string;
    uri?: string;
    predicates?: string;
    filters?: string;
    metadata?: string;
    order?: number;
    enable?: boolean;
    remark?: string;
  }
}
