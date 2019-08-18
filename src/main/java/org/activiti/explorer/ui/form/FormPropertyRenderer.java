package org.activiti.explorer.ui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import java.io.Serializable;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

public abstract interface FormPropertyRenderer
  extends Serializable
{
  public abstract Class<? extends FormType> getFormType();
  
  public abstract Field getPropertyField(FormProperty paramFormProperty);
  
  public abstract String getPropertyLabel(FormProperty paramFormProperty);
  
  public abstract String getFieldValue(FormProperty paramFormProperty, Field paramField);
  
  public abstract Form getForm();
  
  public abstract void setForm(Form paramForm);
}


