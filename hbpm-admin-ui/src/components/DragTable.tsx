import React, { useCallback, useRef } from 'react';
import { DndProvider, useDrag, useDrop } from 'react-dnd';
import { TableProps } from 'antd/es/table';
import update from 'immutability-helper';
import { Table } from 'antd';
import { HTML5Backend } from 'react-dnd-html5-backend';

interface DraggableBodyRowProps extends React.HTMLAttributes<HTMLTableRowElement> {
  index: number;
  moveRow: (dragIndex: number, hoverIndex: number) => void;
}

type DragTableProps<RecordType extends object = any> = Omit<TableProps<RecordType>, 'components' | 'onRow'> & {
  children?: React.ReactNode;
} & {
  ref?: React.Ref<HTMLDivElement> | undefined;
} & {
  onDragChange?: (data: readonly RecordType[]) => void;
};

const type = 'DraggableBodyRow';

const DraggableBodyRow = ({ index, moveRow, className, style, ...restProps }: DraggableBodyRowProps) => {
  const ref = useRef<HTMLTableRowElement>(null);
  const [{ isOver, dropClassName }, drop] = useDrop({
    accept: type,
    collect: (monitor) => {
      const { index: dragIndex } = monitor.getItem() || {};
      if (dragIndex === index) {
        return {};
      }
      return {
        isOver: monitor.isOver(),
        dropClassName: dragIndex < index ? ' drop-over-downward' : ' drop-over-upward',
      };
    },
    drop: (item: { index: number }) => {
      moveRow(item.index, index);
    },
  });
  const [, drag] = useDrag({
    type,
    item: { index },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });
  drop(drag(ref));

  return (
    <tr
      ref={ref}
      className={`${className ?? ''}${isOver ? (dropClassName ?? '') : ''}`}
      style={{ cursor: 'move', ...style }}
      {...restProps}
    />
  );
};

const DragTable: <RecordType extends object = any>(props: DragTableProps<RecordType>) => React.ReactElement = props => {
  const { onDragChange, ...tableProps } = props;

  const components = {
    body: {
      row: DraggableBodyRow,
    },
  };

  const moveRow = useCallback((dragIndex: number, hoverIndex: number) => {
    if (!props.dataSource) {
      return;
    }
    const dragRow = props.dataSource[dragIndex];
    const newData = update(props.dataSource, {
      $splice: [
        [dragIndex, 1],
        [hoverIndex, 0, dragRow],
      ],
    });
    if (onDragChange) {
      onDragChange(newData);
    }
  }, [props.dataSource]);

  return (
    <DndProvider backend={HTML5Backend}>
      <Table
        { ...tableProps }
        className={`sorting-table ${tableProps.className ?? ''}`}
        components={components}
        onRow={(_, index) => {
          const attr = {
            index,
            moveRow,
          };
          return attr as React.HTMLAttributes<any>;
        }}
      />
    </DndProvider>
  );
};

export default DragTable;
