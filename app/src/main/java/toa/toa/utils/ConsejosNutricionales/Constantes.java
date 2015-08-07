package toa.toa.utils.ConsejosNutricionales;

/**
 * Clase que contiene los códigos usados en "toa" para
 * mantener la integridad en las interacciones entre actividades
 * y fragmentos :v
 */
public class Constantes {
    public static final int CODIGO_DETALLE = 100;
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Clave para el valor extra que representa al identificador de un consejo
     */
    public static final String EXTRA_ID = "IDEXTRA";
    private static final int PUERTO_HOST = 80;
    public static final String GET = "http://mundotoa.co:" + PUERTO_HOST + "/api/obtener_consejo.php";
    public static final String GET_BY_ID = "http://10.0.3.2:" + PUERTO_HOST + "/api/obtener_consejo_por_id.php";
    //public static final String UPDATE = "http://10.0.3.2:" + PUERTO_HOST + "/api/actualizar_consejo.php";
    //public static final String DELETE = "http://10.0.3.2:" + PUERTO_HOST + "/api/borrar_consejo.php";
    public static final String INSERT = "http://mundotoa.co:" + PUERTO_HOST + "/api/insertar_consejo.php";

}
