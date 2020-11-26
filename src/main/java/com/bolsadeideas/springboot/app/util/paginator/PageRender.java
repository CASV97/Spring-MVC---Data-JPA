package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * El siguiente paso para el paginador es crear una clase que calcule parametros
 * de los rangos del paginador esta clase tiene la tarea de siempre mostrar un
 * numero acortado de páginas y no todo paginador completo, usamos los Generics
 * de JAva ya que podemos paginar listas de cualquier tipo, el total de paginas
 * y los items por páginas lo obtenemos a travez del objeto Page
 */
public class PageRender<T> {
	private String url;
	private Page<T> page;

	private int totalPages;

	private int numItemsPerPages;

	private int currentPage;

	List<PageItem> pages;

	/**
	 * realizamos el cálculo de obtener los parámetros desde y hasta para poder
	 * dibujar el paginador en la vista, se comprueba si el total de paginas, es
	 * menor o igual al numero de elementos por página, entonce va a mostrar el
	 * paginador completo ya que no va a ser tan grande por ejemplo: tenemos 100
	 * registros y el total de páginas son 10, y la cantidad de registros por página
	 * son 10, en ese caso va a mostrar las 10 páginas en el paginador de una sola
	 * vez
	 */
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		// eso lo inicializamos en el controlador
		numItemsPerPages = page.getSize();
		totalPages = page.getTotalPages();
		/*
		 * es +1 por que en el controlador se especifica la pagina inicial desde 0 por
		 * que en la base de datos parte desde el 0 hasta el límite
		 */
		currentPage = page.getNumber() + 1;

		int from, until;
		// muestra el paginador completo
		if (totalPages <= numItemsPerPages) {
			from = 1;
			until = totalPages;
		} else {
			// otro caso si tenemos mas páginas que el número de items por página va ir
			// recorriendo en el rango
			if (currentPage <= numItemsPerPages / 2) {
				from = 1;
				until = numItemsPerPages;

			}
			// calculamos el rango final
			else if (currentPage >= totalPages - numItemsPerPages / 2) {
				from = totalPages - numItemsPerPages + 1;
				until = numItemsPerPages;
			}
			// rango intermedio
			else {
				from = currentPage - numItemsPerPages / 2;
				until = numItemsPerPages;
			}
		}
		// recorrer el hasta (until)
		for (int i = 0; i < until; i++) {
			pages.add(new PageItem(from + i, currentPage == from + i));
		}

	}

//getters para acceder desde la vista
	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	// getters para saber si la primera pagina es la ultima o si tiene páginas
	// siguiente o si tiene páginas atras
	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	// si tiene siguiente
	public boolean isHasNext() {
		return page.hasNext();
	}

	// si tiene pagina anterior
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
