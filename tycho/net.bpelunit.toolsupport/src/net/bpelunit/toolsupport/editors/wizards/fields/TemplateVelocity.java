package net.bpelunit.toolsupport.editors.wizards.fields;

import java.io.StringReader;

import net.bpelunit.toolsupport.ToolSupportActivator;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.node.ASTAddNode;
import org.apache.velocity.runtime.parser.node.ASTAndNode;
import org.apache.velocity.runtime.parser.node.ASTAssignment;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTComment;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTDivNode;
import org.apache.velocity.runtime.parser.node.ASTEQNode;
import org.apache.velocity.runtime.parser.node.ASTElseIfStatement;
import org.apache.velocity.runtime.parser.node.ASTElseStatement;
import org.apache.velocity.runtime.parser.node.ASTEscape;
import org.apache.velocity.runtime.parser.node.ASTEscapedDirective;
import org.apache.velocity.runtime.parser.node.ASTExpression;
import org.apache.velocity.runtime.parser.node.ASTFalse;
import org.apache.velocity.runtime.parser.node.ASTFloatingPointLiteral;
import org.apache.velocity.runtime.parser.node.ASTGENode;
import org.apache.velocity.runtime.parser.node.ASTGTNode;
import org.apache.velocity.runtime.parser.node.ASTIdentifier;
import org.apache.velocity.runtime.parser.node.ASTIfStatement;
import org.apache.velocity.runtime.parser.node.ASTIntegerLiteral;
import org.apache.velocity.runtime.parser.node.ASTIntegerRange;
import org.apache.velocity.runtime.parser.node.ASTLENode;
import org.apache.velocity.runtime.parser.node.ASTLTNode;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.ASTMethod;
import org.apache.velocity.runtime.parser.node.ASTModNode;
import org.apache.velocity.runtime.parser.node.ASTMulNode;
import org.apache.velocity.runtime.parser.node.ASTNENode;
import org.apache.velocity.runtime.parser.node.ASTNotNode;
import org.apache.velocity.runtime.parser.node.ASTObjectArray;
import org.apache.velocity.runtime.parser.node.ASTOrNode;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.ASTStringLiteral;
import org.apache.velocity.runtime.parser.node.ASTSubtractNode;
import org.apache.velocity.runtime.parser.node.ASTText;
import org.apache.velocity.runtime.parser.node.ASTTrue;
import org.apache.velocity.runtime.parser.node.ASTWord;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.parser.node.ParserVisitor;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Field, to analyze the sentences velocity and paint
 *
 * @author Alejandro Acosta (alex_acos@informaticos.com)
 */
public class TemplateVelocity extends StyledText implements Listener, ParserVisitor {

	// http://www.wikilengua.org/index.php/Lista_de_colores
	private static Color creference = new Color(null, 255, 191, 0);// Ambar
	private static Color cifsetforeach = new Color(null, 102, 2, 60);// Purpura de
																	// Tiro
	private static Color cstringtextinteger = new Color(null, 135, 206, 255);// Celeste
	private static Color ccomment = new Color(null, 0, 168, 107);// Jade

	private String message;
	private StyledText styledText;
	private int endL = -1;

	public TemplateVelocity(Composite parent, int style) {
		super(parent, style);
		addListener(SWT.CHANGED, this);
		styledText = this;
		decorate();
	}

	public void setColorReference(Color color) {
		creference = color;
	}

	public void setColorIfSetStop(Color color) {
		cifsetforeach = color;
	}

	public void setColorStringTextInteger(Color color) {
		cstringtextinteger = color;
	}

	public void setColorComment(Color color) {
		ccomment = color;
	}

	public boolean getError() {
		return message != null;
	}

	public String getMsgError() {
		return message;
	}

	private void decorate() {
		try {
			styledText.setStyleRanges(0, styledText.getText().length(), null, null);
			parse(styledText.getText(), -1);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			ToolSupportActivator.logErrorMessage(e.getMessage());
		}
	}

	private void parse(String Texto, int num) throws ParseException {

		endL = num;

		RuntimeServices rs = RuntimeSingleton.getRuntimeServices();
		try {
			rs.init();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ToolSupportActivator.logErrorMessage(e1.getMessage());
		}

		Parser p = new Parser(rs);
		try {
			SimpleNode node = p.parse(new StringReader(Texto), "prueba");
			message = null;

			node.jjtAccept((ParserVisitor) this, null);
		} catch (ParseException e) {

			String numberL;
			String numberC;

			int pL;
			int pC;

			if (e.getMessage().indexOf("Lexical") == -1) {

				pL = e.currentToken.beginLine;
				pC = e.currentToken.beginColumn;
				message = "There is a syntax error on line " + pL + " and column " + pC
						+ " of the Velocity template";

			} else {

				numberL = e.getMessage().substring(e.getMessage().lastIndexOf("line ") + 5,
						e.getMessage().indexOf(", column"));
				numberC = e.getMessage().substring(e.getMessage().lastIndexOf("column ") + 7,
						e.getMessage().indexOf(".", e.getMessage().lastIndexOf("column ") + 7));

				message = "There is a lexical error on line " + numberL + " and column " + numberC
						+ " of the Velocity template";
				pL = Integer.parseInt(numberL);
			}

			StyleRange styleRangeO = new StyleRange();

			if ((pL - 1) > 0) {

				endL = styledText.getOffsetAtLine(pL - 1) - 1;

			}
			styleRangeO.start = endL + 1;
			if (styledText.getLineCount() == pL) {

				styleRangeO.length = styledText.getText().length() - (endL + 1);
			} else {

				styleRangeO.length = styledText.getOffsetAtLine(pL) - endL;
			}

			styleRangeO.underline = true;
			styledText.setStyleRange(styleRangeO);

		} catch (Exception e) {
			message = e.getMessage();

		}

	}

	public Object visit(SimpleNode arg0, Object arg1) {
		return arg0.childrenAccept((ParserVisitor) this, arg1);
	}

	public Object visit(ASTprocess arg0, Object arg1) {
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTEscapedDirective arg0, Object arg1) {
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTEscape arg0, Object arg1) {
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTComment arg0, Object arg1) {
		int pL, pC;
		pL = arg0.getFirstToken().beginLine;
		pC = arg0.getFirstToken().beginColumn;

		if ((pL - 1) > 0) {

			endL = styledText.getOffsetAtLine(pL - 1) - 1;

		}else{
			endL=-1;
		}

		if (styledText.getLineCount() == pL) {
			color (pL, pC, styledText.getText().length() - (endL + pC), ccomment, endL + pC);
			//styleRangeO.length = styledText.getText().length() - (endL + pC);
		} else {
			color (pL, pC,styledText.getOffsetAtLine(pL) - (endL + pC), ccomment, endL + pC);
			//styleRangeO.length = styledText.getOffsetAtLine(pL) - (endL + pC);
		}
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTFloatingPointLiteral arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTIntegerLiteral arg0, Object arg1) {
		int pL, pC, fC;
		pL = arg0.getFirstToken().beginLine;
		pC = arg0.getFirstToken().beginColumn;
		fC = arg0.getFirstToken().endColumn;

		if ((pL - 1) > 0) {

			endL = styledText.getOffsetAtLine(pL - 1) - 1;

		}else{
			endL=-1;
		}
		
		color (pL, pC, fC - pC + 1, cstringtextinteger, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTStringLiteral arg0, Object arg1) {
		int pL, pC, fC;
		pL = arg0.getFirstToken().beginLine;
		pC = arg0.getFirstToken().beginColumn;
		fC = arg0.getFirstToken().endColumn;

		if ((pL - 1) > 0) {

			endL = styledText.getOffsetAtLine(pL - 1) - 1;

		}else{
			endL=-1;
		}
		
		color (pL, pC, fC - pC + 1, cstringtextinteger, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTIdentifier arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTWord arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTDirective arg0, Object arg1) {
		int pL, pC, fC;
		pL = arg0.getFirstToken().beginLine;

		pC = arg0.getFirstToken().beginColumn;
		fC = arg0.getFirstToken().endColumn;
				
		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}else{
			endL=-1;
		}
		
		color (pL, pC, fC - pC + 1, cifsetforeach, endL + pC);
		pL = arg0.getLastToken().beginLine;

		pC = arg0.getLastToken().beginColumn;
		fC = arg0.getLastToken().endColumn;
		
		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}else{
			endL=-1;
		}
		
		color (pL, pC, fC - pC + 1, cifsetforeach, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTBlock arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTMap arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTObjectArray arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTIntegerRange arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTMethod arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTReference arg0, Object arg1) {
		int pL, pC, fC;
		pL = arg0.getFirstToken().beginLine;
		pC = arg0.getFirstToken().beginColumn;
		fC = arg0.getFirstToken().endColumn;

		if ((pL - 1) > 0) {

			endL = styledText.getOffsetAtLine(pL - 1) - 1;

		}else{
			endL=-1;
		}
		
		color (pL, pC, fC - pC + 1, creference, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTTrue arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTFalse arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTText arg0, Object arg1) {
		int pL, fL, pC, fC;
		pL = arg0.getFirstToken().beginLine;
		fL = arg0.getFirstToken().endLine;
		pC = arg0.getFirstToken().beginColumn;
		fC = arg0.getFirstToken().endColumn;

		if (pL != fL) {
			while (pL <= fL) {

				StyleRange styleRangeO = new StyleRange();
				endL = styledText.getOffsetAtLine(pL - 1) - 1;

				if (pL == fL) {
					styleRangeO.start = endL + 1;
					styleRangeO.length = fC;
				} else if (pL != fL && styledText.getOffsetAtLine(pL) > 1) {
					styleRangeO.start = endL + pC;
					styleRangeO.length = styledText.getOffsetAtLine(pL) - endL - pC + 1;
				}

				styleRangeO.fontStyle = SWT.ITALIC;
				styleRangeO.foreground = cstringtextinteger;
				styledText.setStyleRange(styleRangeO);

				pL++;
			}

		} else if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
			color (pL, pC, fC - pC + 1, cstringtextinteger, endL + pC);
		} else {
			endL=-1;
			color (pL, pC, fC - pC + 1, cstringtextinteger, endL + pC);
		}
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTIfStatement arg0, Object arg1) {
		int pL, pC;
		pL = arg0.getFirstToken().beginLine;

		pC = arg0.getFirstToken().beginColumn;

		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}else{
			endL=-1;
		}
		
		color (pL, pC, 3, cifsetforeach, endL + pC);
		pL = arg0.getLastToken().beginLine;
		pC = arg0.getLastToken().beginColumn;

		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}
		
		color (pL, pC, 4, cifsetforeach, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTElseStatement arg0, Object arg1) {
		int pL = arg0.getFirstToken().beginLine;
		int pC = arg0.getFirstToken().beginColumn;

		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}else{
			endL=-1;
		}

		
		color (pL, pC, 5, cifsetforeach, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTElseIfStatement arg0, Object arg1) {
		int pL = arg0.getFirstToken().beginLine;
		int pC = arg0.getFirstToken().beginColumn;

		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
		}else{
			endL=-1;
		}
		
		color (pL, pC, 7, cifsetforeach, endL + pC);
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTSetDirective arg0, Object arg1) {
		int pL = arg0.getFirstToken().beginLine;
		int pC = arg0.getFirstToken().beginColumn;
		int fC = arg0.getFirstToken().endColumn;

		if ((pL - 1) > 0) {
			endL = styledText.getOffsetAtLine(pL - 1) - 1;
			color (pL, pC,  4, cifsetforeach, endL + pC);
		} else if (pL - 1 == 0) {
			endL=-1;
			color (pL, pC, 4, cifsetforeach, endL + fC - 4);
		}
		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTExpression arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTAssignment arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTOrNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTAndNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTEQNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTNENode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTLTNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTGTNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTLENode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTGENode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTAddNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTSubtractNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTMulNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTDivNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTModNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}

	public Object visit(ASTNotNode arg0, Object arg1) {

		return visit((SimpleNode) arg0, arg1);
	}
	
	public void setText(String text) {
		super.setText(text);
		decorate();
	}

	public void handleEvent(Event ev) {
		if (ev.type == SWT.CHANGED) {
			decorate();
		}
	}
	
	private void color (int pL,int pC,int lenght,Color color, int start)
	{
		StyleRange styleRangeO = new StyleRange();
		styleRangeO.start = start;
		styleRangeO.length = lenght;
		styleRangeO.fontStyle = SWT.BOLD;
		styleRangeO.foreground = color;
		styledText.setStyleRange(styleRangeO);
	}
}

