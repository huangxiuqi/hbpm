import React, { useEffect, useState } from 'react';
import {
  DrawerForm,
  ProFormDigit,
  ProFormRadio,
  ProFormSwitch,
  ProFormText,
  ProFormTreeSelect,
} from '@ant-design/pro-components';
import { Form, Input, message, Spin } from 'antd';
import { getResource, getResourceTree, saveResource } from '@/services/system/resource';
import * as Icons from '@ant-design/icons';
import SelectIcon from './SelectIcon';
import { RequestOptionsType } from '@ant-design/pro-utils/es/typing';

const Edit: React.FC<Hbpm.EditComponentProps> = props => {
  const [form] = Form.useForm<API.Resource>();
  const iconValue = Form.useWatch('icon', form);
  const type = Form.useWatch('type', form);
  const [isPageLoading, setPageLoading] = useState(true);

  let iconComponent = <Icons.AppstoreOutlined/>;
  if (iconValue) {
    iconComponent = React.createElement((Icons as any)[iconValue]);
  }

  useEffect(() => {
    if (props.id && props.open) {
      setPageLoading(true);
      void getResource(props.id)
        .then(data => {
          if (data.parentId === '0') {
            data.type = 0;
            data.parentId = undefined;
          } else {
            data.type = 2;
          }
          form.setFieldsValue(data);
          setPageLoading(false);
        });
    } else {
      setPageLoading(false);
    }
  }, [form, props.id, props.open]);

  return (
    <DrawerForm<API.Resource>
      title={props.id ? '编辑菜单' : '新增菜单'}
      form={form}
      open={props.open}
      onOpenChange={open => props.onOpenChange?.call(null, open, false)}
      autoFocusFirstInput
      submitTimeout={30000}
      width={500}
      layout="horizontal"
      labelCol={{
        span: 5,
      }}
      drawerProps={{
        maskClosable: false,
        keyboard: false,
        destroyOnClose: true,
      }}
      onFinish={async (values) => {
        if (values.type === 0) {
          values.parentId = '0';
        } else if (values.type === 2) {
          values.type = 0;
        }
        return saveResource(values).then(() => {
          void message.success('提交成功');
          props.onOpenChange?.call(null, false, true);
          return false;
        });
      }}
    >
      {
        isPageLoading
          ? <div className="page-loading">
            <Spin size="large"/>
          </div>
          : <>
            <ProFormText name="id" hidden initialValue=""></ProFormText>
            <ProFormRadio.Group
              name="type"
              label="菜单类型"
              initialValue={0}
              radioType="button"
              options={[
                {
                  label: '一级菜单',
                  value: 0,
                },
                {
                  label: '子级菜单',
                  value: 2,
                },
                {
                  label: '按钮',
                  value: 1,
                },
              ]}
            />
            <ProFormText
              name="name"
              label="菜单名称"
              initialValue=""
              rules={[{ required: true, message: '请输入菜单名称' }]}
            ></ProFormText>
            {
              type === 2
                ? <ProFormTreeSelect
                  name="parentId"
                  label="上级"
                  debounceTime={500}
                  fieldProps={{
                    allowClear: true,
                    showSearch: true,
                    filterTreeNode: false,
                  }}
                  rules={[{ required: true, message: '请选择上级菜单' }]}
                  request={async (params, p) => {
                    const transform = (resource: API.Resource[] | undefined): RequestOptionsType[] => {
                      return (resource ?? []).filter(i => i.id !== props.id).map(i => {
                        const obj: RequestOptionsType = {
                          label: i.name,
                          value: i.id,
                        };
                        const children = transform(i.children);
                        if (children.length > 0) {
                          obj.children = children;
                        }
                        return obj;
                      });
                    };
                    return getResourceTree({
                      name: params.keyWords,
                    }).then(data => {
                      const val = transform(data);
                      return val || [];
                    });
                  }}
                ></ProFormTreeSelect>
: null
            }
            <ProFormText name="url" label="访问路径" initialValue=""></ProFormText>
            <Form.Item label="图标" help="点击右侧按钮选择图标">
              <Input.Group compact>
                <Form.Item noStyle name="icon" initialValue="">
                  <Input
                    allowClear
                    readOnly
                    placeholder="请选择"
                    suffix={
                      iconValue
                        ? <Icons.CloseCircleFilled
                          className="ant-input-clear-icon"
                          onClick={() => {
                            form.setFieldValue('icon', '');
                          }}/>
                        : null
                    }
                    style={{ width: 'calc(100% - 32px)' }}
                  />
                </Form.Item>
                <SelectIcon icon={iconComponent} onChange={name => {
                  form.setFieldValue('icon', name);
                }}/>
              </Input.Group>
            </Form.Item>
            <ProFormDigit
              label="排序"
              name="orderNumber"
              min={1}
              max={99999}
              initialValue={99999}
              width={150}
              fieldProps={{ precision: 0 }}
            />
            <ProFormSwitch name="newWindow" label="新窗口打开" initialValue={false}/>
          </>
      }
    </DrawerForm>
  );
};

export default Edit;
