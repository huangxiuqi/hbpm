import React, { useRef, useState } from 'react';
import { Alert, Button, message, Space } from 'antd';
import { batchDeleteResource, deleteResource, getResourceTree } from '@/services/system/resource';
import * as Icons from '@ant-design/icons';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import Edit from './Edit';
import ConfirmButton from '@/components/ConfirmButton';

const typeMap: Record<string, string> = {
  0: '菜单',
  1: '按钮',
};

const List: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [isEditOpen, setEditOpen] = useState(false);
  const [editId, setEditId] = useState<string | undefined>(undefined);
  const [expandedRowKeys, setExpandedRowKeys] = useState<readonly React.Key[]>([]);
  const [ids, setIds] = useState<string[]>([]);
  const columns: Array<ProColumns<API.Resource>> = [
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '类型',
      dataIndex: 'type',
      width: '100px',
      className: 'text-center',
      search: false,
      render: (_, { type }) => typeMap[type!],
    },
    {
      title: '图标',
      dataIndex: 'icon',
      width: '80px',
      className: 'text-center',
      search: false,
      render: (_, { icon }) => icon ? React.createElement((Icons as any)[icon]) : '-',
    },
    {
      title: '路径',
      dataIndex: 'url',
      search: false,
    },
    {
      title: '打开方式',
      dataIndex: 'newWindow',
      search: false,
      width: '100px',
      className: 'text-center',
      render: (_, { newWindow }) => newWindow ? '新窗口' : '当前窗口',
    },
    {
      title: '排序',
      dataIndex: 'orderNumber',
      width: '100px',
      className: 'text-center',
      search: false,
    },
    {
      title: '操作',
      key: 'action',
      width: '140px',
      className: 'text-center',
      search: false,
      render: (_, record) => {
        return (
          <>
            <Button
              type="link"
              size="small"
              onClick={() => {
                setEditId(record.id);
                setEditOpen(true);
              }}>编辑</Button>
            <ConfirmButton
              buttonProps={{
                type: 'link',
                size: 'small',
                danger: true,
              }}
              onConfirm={async () => {
                await deleteResource(record.id!)
                  .then(async () => {
                    tableReset();
                    void message.success('删除成功');
                  });
              }}
            >删除</ConfirmButton>
          </>
        );
      },
    },
  ];

  const tableReset = () => {
    if (actionRef.current) {
      actionRef.current.reset?.call(null);
      actionRef.current?.clearSelected?.call(null);
    }
  };
  return (
    <>
      <ProTable<API.Resource>
        columns={columns}
        actionRef={actionRef}
        bordered
        size="small"
        request={async (params = {}, sort, filter) => {
          return getResourceTree({
            name: params.name,
          }).then(tree => {
            const idList: string[] = [];
            const dfs = (resource: API.Resource[] | undefined) => {
              (resource ?? []).forEach(i => {
                idList.push(i.id!);
                dfs(i.children);
              });
            };
            dfs(tree);
            setIds(idList);
            return {
              data: tree,
            };
          });
        }}
        rowKey="id"
        search={false}
        options={{
          setting: false,
          density: false,
          fullScreen: true,
          search: {
            name: 'name',
            allowClear: true,
            placeholder: '请输入菜单名称',
          },
        }}
        form={{
          disabled: true,
        }}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange (expandedKeys) {
            setExpandedRowKeys(expandedKeys);
          },
        }}
        pagination={false}
        dateFormatter="string"
        headerTitle={
          <>
            <Space size={14}>
              <Button
                key="button"
                icon={<Icons.PlusOutlined/>}
                type="primary"
                onClick={() => {
                  setEditId(undefined);
                  setEditOpen(true);
                }}>
                新建菜单
              </Button>
              <Button onClick={() => {
                setExpandedRowKeys(ids);
              }}><Icons.ColumnHeightOutlined/>展开全部</Button>
              <Button onClick={() => {
                setExpandedRowKeys([]);
              }}><Icons.VerticalAlignMiddleOutlined/>折叠全部</Button>
              {
                selectedRowKeys && selectedRowKeys?.length > 0
                  ? <ConfirmButton
                    buttonProps={{
                      danger: true,
                    }}
                    onConfirm={async () => {
                      await batchDeleteResource(selectedRowKeys).then(() => {
                        tableReset();
                        void message.success('删除成功');
                      });
                    }}
                  >批量删除</ConfirmButton>
                  : null
              }
            </Space>
          </>
        }
        rowSelection={{
          alwaysShowAlert: true,
          onChange (selectedRowKeys) {
            setSelectedRowKeys(selectedRowKeys);
          },
        }}
        tableAlertRender={({ selectedRowKeys, onCleanSelected }) => (
          <Alert message={
            <span>
              已选 {selectedRowKeys.length} 项
              {
                selectedRowKeys.length > 0
                  ? <a style={{ marginInlineStart: 8 }} onClick={onCleanSelected}>
                    取消选择
                  </a>
                  : null
              }
            </span>
          } type="info" showIcon/>
        )}
        tableAlertOptionRender={false}
      />

      <Edit id={editId} open={isEditOpen} onOpenChange={(open, isSaved) => {
        setEditOpen(open);
        if (isSaved) {
          void actionRef.current?.reload();
        }
      }}/>
    </>
  );
};

export default List;
