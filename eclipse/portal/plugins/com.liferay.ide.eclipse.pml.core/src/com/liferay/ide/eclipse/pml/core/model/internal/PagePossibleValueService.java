/*******************************************************************************
 * Copyright (c) 2000-2011 Accenture Services Pvt Ltd., All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Contributors:
 *    Kamesh Sampath - initial implementation
 ******************************************************************************/

package com.liferay.ide.eclipse.pml.core.model.internal;

import com.liferay.ide.eclipse.pml.core.model.IOrganization;
import com.liferay.ide.eclipse.pml.core.model.IPage;
import com.liferay.ide.eclipse.pml.core.model.ISite;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.PossibleValuesService;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */

public final class PagePossibleValueService extends PossibleValuesService {

	String[] params;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.ModelPropertyService#init(org.eclipse.sapphire.modeling.IModelElement,
	 * org.eclipse.sapphire.modeling.ModelProperty, java.lang.String[])
	 */
	@Override
	public void init( IModelElement element, ModelProperty property, String[] params ) {

		super.init( element, property, params );
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.PossibleValuesService#fillPossibleValues(java.util.SortedSet)
	 */
	@Override
	protected void fillPossibleValues( SortedSet<String> values ) {
		IOrganization currentOrganization = element().nearest( IOrganization.class );
		ISite currentSite = element().nearest( ISite.class );
		List<IPage> pages = null;
		if ( currentOrganization != null ) {
			pages = currentOrganization.getPages();

		}
		else if ( currentSite != null ) {
			pages = currentSite.getPages();
		}

		if ( pages != null ) {
			for ( IPage iPage : pages ) {
				List<IPage> pagesCollection = new ArrayList<IPage>();
				recurseAndGetPages( iPage, pagesCollection );
				for ( IPage iPage2 : pagesCollection ) {
					values.add( iPage2.getName().getText() );
				}

			}
		}

	}

	/**
	 * 
	 * @param currPage
	 * @param pages
	 */
	private void recurseAndGetPages( IPage currPage, List<IPage> pages ) {
		pages.add( currPage );
		if ( currPage.getPages() != null && !currPage.getPages().isEmpty() ) {
			ListIterator<IPage> pageIterator = currPage.getPages().listIterator();
			while ( pageIterator.hasNext() ) {
				IPage page = pageIterator.next();
				recurseAndGetPages( page, pages );
			}
		}
	}

}
