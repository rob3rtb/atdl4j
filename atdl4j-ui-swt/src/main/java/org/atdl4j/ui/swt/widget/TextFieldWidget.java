package org.atdl4j.ui.swt.widget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.atdl4j.ui.swt.util.NumberFormatVerifyListener;
import org.atdl4j.ui.swt.util.ParameterListenerWrapper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.fixprotocol.atdl_1_1.core.IntT;
import org.fixprotocol.atdl_1_1.core.LengthT;
import org.fixprotocol.atdl_1_1.core.SeqNumT;
import org.fixprotocol.atdl_1_1.core.NumInGroupT;
import org.fixprotocol.atdl_1_1.core.TagNumT;
import org.fixprotocol.atdl_1_1.core.NumericT;
import org.fixprotocol.atdl_1_1.core.ParameterT;
import org.fixprotocol.atdl_1_1.layout.TextFieldT;

public class TextFieldWidget extends AbstractSWTWidget<String> {

	private Text textField;
	private Label label;

	public TextFieldWidget(TextFieldT control, ParameterT parameter) throws JAXBException {
		this.control = control;
		this.parameter = parameter;
		init();
	}

	public Widget createWidget(Composite parent, int style)
			throws JAXBException {
				
		// label
		Label l = new Label(parent, SWT.NONE);
		this.label = l;
// 1/20/2010 Scott Atwell avoid NPE as label is not required on Control		l.setText(control.getLabel());
		if ( control.getLabel() != null )
		{
			l.setText(control.getLabel());
		}


		// textField
		Text textField = new Text(parent, style | SWT.BORDER);
		this.textField = textField;
		textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// type validation
		if (parameter instanceof IntT ||
			parameter instanceof TagNumT ||
			parameter instanceof LengthT ||	
			parameter instanceof SeqNumT ||
			parameter instanceof NumInGroupT) {
			// Integer types
			textField.addVerifyListener(new NumberFormatVerifyListener(
					new DecimalFormat("#"), false));
		} else if (parameter instanceof NumericT) {
			// Decimal types
			textField.addVerifyListener(new NumberFormatVerifyListener(
					new DecimalFormat("0.0"), false));
		}
		// TODO: add regex verifier for MultipleCharValueT and MultipleStringValueT
			
		// init value
		if (((TextFieldT)control).getInitValue() != null)
			textField.setText(((TextFieldT)control).getInitValue());

		// tooltip
		String tooltip = control.getTooltip();
		textField.setToolTipText(tooltip);
		l.setToolTipText(tooltip);

		return parent;
	}

	public String getControlValue() {
//TODO 1/24/2010 Scott Atwell added
		if ( ( textField.isVisible() == false ) || ( textField.isEnabled() == false ) )
		{
			return null;
		}
		
		String value = textField.getText();

		if ("".equals(value)) {
			return null;
		} else {
			return value;
		}
	}

	public String getParameterValue() {
		return getControlValue();
	}

	public void setValue(String value) {
		textField.setText((value == null) ? "" : value.toString());
	}
	
	public void generateStateRuleListener(Listener listener) {
		textField.addListener(SWT.Modify, listener);
	}

	public List<Control> getControls() {
		List<Control> widgets = new ArrayList<Control>();
		widgets.add(label);
		widgets.add(textField);
		return widgets;
	}

	public void addListener(Listener listener) {
		textField.addListener(SWT.Modify, new ParameterListenerWrapper(this,
				listener)); // TODO: SWT.Selection??
	}

	public void removeListener(Listener listener) {
		textField.removeListener(SWT.Selection, listener); // TODO: SWT.Modify??
	}
}