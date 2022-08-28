package me.hawkcore.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginaCreator {

	private List<Object> objects = new ArrayList<>();
	private int max, paginaAtual = 1;
	
	public PaginaCreator(List<Object> objects, int max) {
		this.objects = objects;
		this.max = max;
	}

	public List<Object> getPagina(int pagina) {
		if (containsPagina(pagina) && !objects.isEmpty() && getTotalPaginas() > 0) {
			HashMap<Integer, List<Object>> hash = new HashMap<Integer, List<Object>>();
			for (int p = 1; p <= getTotalPaginas(); p++) {
				if (!hash.containsKey(p)) hash.put(p, new ArrayList<>());
				if (hash.containsKey(p-1)) {
					int total = 0;
					for(int p1 : hash.keySet()) {
						if (p1!=p) total+=hash.get(p1).size();
					}
					for (int i = 0; i < objects.size(); i++) {
						if (i >= total && i-total < max) {
							hash.get(p).add(objects.get(i));
						}
					}
				}else {
					for (int i = 0; i < objects.size(); i++) {
						if (i < max) {
							hash.get(p).add(objects.get(i));
						}
					}
				}
			}
			return hash.get(pagina);
		}
		return new ArrayList<>();
	}
	
	public int getTotalPaginas() {
		return objects.size() % max > 0 ? (objects.size()/max)+1 : objects.size()/max;
	}
	
	public Boolean containsPagina(int pagina) {
		int total = getTotalPaginas();
		return pagina <= total && pagina > 0;
	}
	
}
