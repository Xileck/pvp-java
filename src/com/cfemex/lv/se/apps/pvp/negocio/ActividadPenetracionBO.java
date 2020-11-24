package com.cfemex.lv.se.apps.pvp.negocio;

import com.cfemex.lv.DAOException;
import com.cfemex.lv.libs.informix.Informix;
import com.cfemex.lv.se.apps.pvp.ActividadPenetracion;
import com.cfemex.lv.se.apps.pvp.DAO.ActividadPenetracionDAO;

import java.util.List;


public class ActividadPenetracionBO implements IActividadPenetracionBO {
    private ActividadPenetracionDAO actividadPenetracionDAO;

    public static ActividadPenetracionBO instance = null;

    public static ActividadPenetracionBO getInstance() {
        if (instance == null)
            instance = new ActividadPenetracionBO();
        return instance;
    }

    public ActividadPenetracionBO() {
        actividadPenetracionDAO = ActividadPenetracionDAO.getInstance();
    }

    /**
     * @return the actividadPenetracionDAO
     */
    public ActividadPenetracionDAO getActividadPenetracion() {
        return actividadPenetracionDAO;
    }

    /**
     * @param actividadPenetracionDAO the actividadPenetracionDAO to set
     */
    public void setActividadPenetracionDAO(ActividadPenetracionDAO actividadPenetracionDAO) {
        this.actividadPenetracionDAO = actividadPenetracionDAO;
    }

    @Override
    public List<ActividadPenetracion> seleccionarActividadPenetracionCondicion(List<String> condiciones) {
        return null;
    }
}
