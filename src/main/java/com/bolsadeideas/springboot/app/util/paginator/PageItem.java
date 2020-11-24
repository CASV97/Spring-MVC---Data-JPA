package com.bolsadeideas.springboot.app.util.paginator;

/**
 * Representa cada una de las páginas, va a tener su número de página y un
 * atributo para indicar si es o no página actual
 */
public class PageItem {
	private int numPage;
	private boolean isCurrentPage;

	public PageItem(int numPage, boolean isCurrentPage) {
		this.numPage = numPage;
		this.isCurrentPage = isCurrentPage;
	}

	public int getNumPage() {
		return numPage;
	}

	public boolean isCurrentPage() {
		return isCurrentPage;
	}
	

}
