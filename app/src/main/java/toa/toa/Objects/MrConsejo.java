package toa.toa.Objects;

import java.util.ArrayList;

/**
 * Reflejo de la tabla 'consejo' en la base de datos
 */
public class MrConsejo {

    private static final String TAG = MrConsejo.class.getSimpleName();
    /*
        Atributos
         */
    private String idConsejo;
    private String titulo;
    private String descripcion;
    private String autor;
    private String fechaLim;
    private String categoria;
    private ArrayList<String> tags;

    public MrConsejo(String idConsejo,
                     String titulo,
                     String descripcion,
                     String fechaLim,
                     String categoria,
                     String autor) {
        this.idConsejo = idConsejo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.autor = autor;
        this.fechaLim = fechaLim;
        this.categoria = categoria;
    }

    public ArrayList<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
            tags.add("Desayuno");
            tags.add("TAG");
        } else if (tags.isEmpty()) {
            tags.add("Desayuno");
            tags.add("TAG");
        }
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getIdConsejo() {
        return idConsejo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAutor() {
        return autor;
    }

    public String getFechaLim() {
        return fechaLim;
    }

    public String getCategoria() {
        return categoria;
    }

    /**
     * Compara los atributos de dos metas
     *
     * @param mrConsejo Consejo externa
     * @return true si son iguales, false si hay cambios
     */
    public boolean compararCon(MrConsejo mrConsejo) {
        return this.titulo.compareTo(mrConsejo.titulo) == 0 &&
                this.descripcion.compareTo(mrConsejo.descripcion) == 0 &&
                this.fechaLim.compareTo(mrConsejo.fechaLim) == 0 &&
                this.categoria.compareTo(mrConsejo.categoria) == 0 &&
                this.autor.compareTo(mrConsejo.autor) == 0;
    }
}
