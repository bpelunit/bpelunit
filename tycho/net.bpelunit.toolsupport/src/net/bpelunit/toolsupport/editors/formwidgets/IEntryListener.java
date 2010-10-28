/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package net.bpelunit.toolsupport.editors.formwidgets;

public interface IEntryListener {

	/**
	 * The user changed the text in the text control of the entry.
	 * 
	 * @param entry
	 */
	void textDirty(TextEntry entry);

	/**
	 * The value of the entry has been changed to be the text in the text control (as a result of
	 * 'commit' action).
	 * 
	 * @param entry
	 */
	void textValueChanged(TextEntry entry);

}
