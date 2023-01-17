import React, { useState } from 'react';
import { Button, Popconfirm } from 'antd';
import { ButtonProps } from 'antd/es/button/button';

type RenderFunction = () => React.ReactNode;

interface PropsType {
  popTitle?: React.ReactNode | RenderFunction
  popDescription?: React.ReactNode | RenderFunction
  buttonProps?: Omit<ButtonProps, 'onClick'> & React.RefAttributes<HTMLElement>
  onConfirm?: () => Promise<void>
  children?: React.ReactNode
}

const ConfirmButton: React.FC<PropsType> = props => {
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);

  const handleConfirm = () => {
    if (props.onConfirm) {
      setConfirmLoading(true);
      props.onConfirm().finally(() => {
        setConfirmLoading(false);
        setOpen(false);
      });
    } else {
      setOpen(false);
    }
  };

  const handleCancel = () => {
    setOpen(false);
  };

  return (
    <Popconfirm
      title={props.popTitle ? props.popTitle : '操作确认'}
      description={props.popDescription ? props.popDescription : '您确定要删除吗？'}
      open={open}
      onConfirm={handleConfirm}
      okButtonProps={{ loading: confirmLoading }}
      cancelButtonProps={{ disabled: confirmLoading }}
      onCancel={handleCancel}
    >
      <Button {...props.buttonProps} onClick={() => {
        setOpen(true);
      }}>{props.children}</Button>
    </Popconfirm>
  );
};

export default ConfirmButton;
