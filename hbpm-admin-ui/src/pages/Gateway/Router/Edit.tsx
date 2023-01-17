import React, { useEffect, useState } from 'react';
import { DrawerForm, ProFormDigit, ProFormSwitch, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { Form, message, Spin } from 'antd';
import { getRoute, saveRoute } from '@/services/gateway/route';
import PredicateEdit from '@/pages/Gateway/Router/PredicateEdit';
import FilterEdit from '@/pages/Gateway/Router/FilterEdit';

const Edit: React.FC<Hbpm.EditComponentProps> = props => {
  const [form] = Form.useForm<API.Resource>();
  const [isPageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    if (props.id && props.open) {
      setPageLoading(true);
      void getRoute(props.id)
        .then(data => {
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
      width={600}
      layout="horizontal"
      labelCol={{
        span: 3,
      }}
      drawerProps={{
        maskClosable: false,
        keyboard: false,
        destroyOnClose: true,
      }}
      onFinish={async (values) => {
        console.log(values);
        return Promise.resolve();
        // return saveRoute(values).then(() => {
        //   void message.success('提交成功');
        //   props.onOpenChange?.call(null, false, true);
        //   return false;
        // });
      }}
    >
      {
        isPageLoading
          ? <div className="page-loading">
            <Spin size="large"/>
          </div>
          : <>
            <ProFormText name="id" hidden initialValue=""></ProFormText>
            <ProFormText
              name="uri"
              label="URI"
              initialValue=""
              required
              rules={[
                {
                  validator: async (_, val) => {
                    if (val === '' || val === undefined || val === null) {
                      return Promise.reject(new Error('请输入URI'));
                    }
                    try {
                      const url = new URL(val);
                      console.log(url);
                    } catch (e) {
                      console.log(e);
                      return Promise.reject(new Error('URI不合法'));
                    }
                  },
                },
              ]}></ProFormText>
            <Form.Item name="predicates" label="断言" initialValue="[]">
              <PredicateEdit />
            </Form.Item>
            <Form.Item name="filters" label="过滤器" initialValue="[]">
              <FilterEdit />
            </Form.Item>
            <ProFormTextArea name="metadata" label="元数据" initialValue="{}" hidden></ProFormTextArea>
            <ProFormTextArea
              name="remark"
              label="备注"
              initialValue=""
              fieldProps={{
                maxLength: 200,
                showCount: true,
              }}></ProFormTextArea>
            <ProFormText name="createTime" hidden initialValue=""></ProFormText>
            <ProFormSwitch name="enable" label="是否启用" initialValue={true}></ProFormSwitch>
            <ProFormDigit
              label="排序"
              name="order"
              min={0}
              max={99999}
              initialValue={0}
              width={150}
              fieldProps={{ precision: 0 }}
            />
          </>
      }
    </DrawerForm>
  );
};

export default Edit;
