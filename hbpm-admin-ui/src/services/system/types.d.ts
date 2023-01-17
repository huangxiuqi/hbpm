declare namespace API {
  interface Menu {
    id?: string;
    name?: string;
    parentId?: string;
    show?: boolean;
    type?: number;
    url?: string;
    icon?: string;
    orderNumber?: number;
    newWindow?: boolean;
    children?: Menu[];
  };

  interface Resource {
    id?: string;
    name?: string;
    parentId?: string;
    show?: boolean;
    type?: number;
    url?: string;
    icon?: string;
    orderNumber?: number;
    newWindow?: boolean;
    children?: Menu[];
  };
}
