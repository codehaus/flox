/*
 * Copyright (c) 2004-2005, by OpenXource, LLC. All rights reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF OPENXOURCE
 *  
 * The copyright notice above does not evidence any          
 * actual or intended publication of such source code. 
 */
package flox.web.tapestry.model;

import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.hibernate.Criteria;

import flox.model.PageableDao;

/**
 * Pithy description goes here.
 */
public class BaseTableModel implements IBasicTableModel
{
    private PageableDao pageableDao;
    private Criteria criteria;

    public BaseTableModel(PageableDao pageableDao)
    {
        this.pageableDao = pageableDao;
    }
    
    public BaseTableModel(PageableDao pageableDao, Criteria criteria)
    {
        this( pageableDao );
        this.criteria    = criteria;
    }

    public int getRowCount()
    {
        return pageableDao.countAll( this.criteria );
    }

    public Iterator getCurrentPageRows(int first, int pageSize, ITableColumn objSortColumn, boolean bSortOrder)
    {
        String sortColumn = objSortColumn == null ? null : objSortColumn.getColumnName();
        return pageableDao.getCurrentPageRows( first, pageSize, sortColumn, bSortOrder, this.criteria );
    }
}
