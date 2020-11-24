package com.cfemex.lv.se.apps.pvp.negocio;
import com.cfemex.lv.se.apps.pvp.ActividadPenetracion;
import java.util.List;

public interface IActividadPenetracionBO {
		List<ActividadPenetracion> seleccionarActividadPenetracionCondicion(List<String> condiciones);

}
