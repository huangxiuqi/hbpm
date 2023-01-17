import React from 'react';
import { Button, Input, Select } from 'antd';
import { ColumnsType } from 'antd/es/table';
import DragTable from '@/components/DragTable';
import ConfirmButton from '@/components/ConfirmButton';

interface PredicateEditProps {
  value?: string;
  onChange?: (value: string) => void;
}

interface DataType {
  key: number;
  name?: string;
  args?: Record<string, string | number>;
}

interface PredicateDefine {
  label: string;
  value: string;
  args?: PredicateArgs[];
}

interface PredicateArgs {
  name: string;
  type: string;
  field: string;
}

const predicates: PredicateDefine[] = [
  {
    value: 'Path',
    label: 'Path匹配',
    args: [{
      name: 'Path',
      type: 'string',
      field: 'pattern',
    }],
  },
  {
    value: 'Host',
    label: 'Host匹配',
    args: [{
      name: 'Host',
      type: 'string',
      field: 'patterns',
    }],
  },
  {
    value: 'Cookie',
    label: 'Cookie匹配',
    args: [{
      name: 'Cookie',
      type: 'string',
      field: 'patterns',
    }],
  },
  {
    value: 'Header',
    label: 'Header匹配',
    args: [{
      name: 'Header',
      type: 'string',
      field: 'patterns',
    }],
  },
  {
    value: 'Method',
    label: 'Method匹配',
  },
  {
    value: 'Query',
    label: 'Query匹配',
  },
  {
    value: 'RemoteAddr',
    label: 'RemoteAddr匹配',
  },
];
const predicateMap: Record<string, PredicateDefine> = {};
predicates.forEach(it => {
  predicateMap[it.value] = it;
});

const PredicateEdit: React.FC<PredicateEditProps> = props => {
  let data: DataType[] = [];
  if (props.value) {
    data = JSON.parse(props.value);
    data = data.map((i, idx) => ({ ...i, key: idx }));
  }

  const columns: ColumnsType<DataType> = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      width: '160px',
      render: (value, record) => {
        return <Select
          value={value}
          onChange={val => {
            const idx = data.indexOf(record);
            if (idx > -1) {
              const it = data[idx];
              if (it) {
                it.name = val;
                it.args = undefined;
                emitOnChange(data);
              }
            }
          }}
          options={predicates}
        />;
      },
    },
    {
      title: '参数',
      dataIndex: 'args',
      key: 'args',
      render: (_, record) => {
        if (record.name) {
          const it = predicateMap[record.name];
          if (it) {
            return it.args?.map((f, idx) => (
              <Input
                onDragStart={e => { e.stopPropagation(); }}
                key={idx}
                addonBefore={f.name}
                value={record.args ? record.args[f.field] : ''}
                onChange={val => {
                  const fIdx = data.indexOf(record);
                  if (fIdx > -1) {
                    const args = data[fIdx].args ?? {};
                    args[f.field] = val.target.value;
                    data[fIdx].args = args;
                    emitOnChange(data);
                  }
                }}/>
            ));
          }
        }
        return null;
      },
    },
    {
      title: '操作',
      width: '65px',
      className: 'text-center',
      render: (_, record) => {
        return (
          <>
            <ConfirmButton
              buttonProps={{
                type: 'link',
                size: 'small',
                danger: true,
              }}
              onConfirm={async () => {
                const idx = data.indexOf(record);
                if (idx > -1) {
                  data.splice(idx, 1);
                  emitOnChange(data);
                }
                return Promise.resolve();
              }}
            >删除</ConfirmButton>
          </>
        );
      },
    },
  ];

  const onDragChange = (data: readonly DataType[]) => {
    emitOnChange(data);
  };

  const addItem = () => {
    data.push({
      key: data.length,
      name: 'Path',
    });
    emitOnChange(data);
  };

  const emitOnChange = (data: readonly DataType[]) => {
    const val = JSON.stringify(data.map(i => ({ name: i.name, args: i.args })));
    props.onChange?.call(null, val);
  };

  return (
    <>
      <div><Button type="primary" onClick={addItem}>添加断言</Button></div>
      <div style={{ height: '10px' }}></div>
      {
        data.length > 0
          ? <DragTable
            columns={columns}
            dataSource={data}
            onDragChange={onDragChange}
            pagination={false}
            showHeader={false}
            bordered
            locale={{
              emptyText: null,
            }}
            size="small"
          ></DragTable>
          : null
      }

    </>
  );
};

export default PredicateEdit;
