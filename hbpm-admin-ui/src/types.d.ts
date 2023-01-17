namespace Hbpm {
  interface EditComponentProps {
    id?: string;
    open?: boolean;
    onOpenChange?: (open: boolean, isSaved: boolean) => void;
  }
}
