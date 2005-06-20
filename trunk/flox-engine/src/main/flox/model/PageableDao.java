/*
 * Copyright (c) 2004-2005, by OpenXource, LLC. All rights reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF OPENXOURCE
 *  
 * The copyright notice above does not evidence any          
 * actual or intended publication of such source code. 
 */
package flox.model;

import java.util.Iterator;

import org.hibernate.Criteria;

/**
 * Pithy description goes here.
 */
public interface PageableDao
{
    Iterator getCurrentPageRows(final int first, final int pageSize, String sortColumn, boolean sortOrder, Criteria criteria);
    int countAll(Criteria criteria);
}
