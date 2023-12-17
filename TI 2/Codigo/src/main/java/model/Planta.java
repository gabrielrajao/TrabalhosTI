package model;


public class Planta {

    // atributos
	int plantId;
    String slug; 
    String nome_pop; 
    String nomecien; 
    String imagemurl; 
    String cresc_forma;
    String cresc_taxa;
    float ph_max; //
    float ph_min; //
    int luz_solar; //
    int solo_nutrientes; //
    int solo_salinidade; //
    int solo_textura; //
    int solo_umidade;

    // construtores
    public Planta() {
    	plantId = -1;
        slug = "";
        nome_pop = "";
        nomecien = "";
        imagemurl = "";
        cresc_forma = "";
        cresc_taxa = "";

        ph_max = -1;
        ph_min = -1;
        luz_solar = -1;
        solo_nutrientes = -1;
        solo_salinidade = -1;
        solo_textura = -1;
        solo_umidade = -1;
    }

    public Planta(int plantId,String slug, String nome_pop, String nomecien, String imagemurl,  String cresc_forma,
            String cresc_taxa,  float ph_max, float ph_min,
            int luz_solar, int solo_nutrientes, int solo_salinidade, int solo_textura, int solo_umidade) {
    	this.plantId = plantId;
        this.slug = slug;
        this.nome_pop = nome_pop;
        this.nomecien = nomecien;
        this.imagemurl = imagemurl;
        this.cresc_forma = cresc_forma;
        this.cresc_taxa = cresc_taxa;
        this.ph_max = ph_max;
        this.ph_min = ph_min;
        this.luz_solar = luz_solar;
        this.solo_nutrientes = solo_nutrientes;
        this.solo_salinidade = solo_salinidade;
        this.solo_textura = solo_textura;
        this.solo_umidade = solo_umidade;
    }

    // gets
    public int getPlantId() {
    	return plantId;
    }
    
     public String getSlug() {
        return slug;
    }

    public String getNome_pop() {
        return nome_pop;
    }

    public String getNomecien() {
        return nomecien;
    }

    public String getImagemurl() {
        return imagemurl;
    }


    public String getCresc_forma() {
        return cresc_forma;
    }

    public String getCresc_taxa() {
        return cresc_taxa;
    }



    public float getPh_max() {
        return ph_max;
    }

    public float getPh_min() {
        return ph_min;
    }

    public int getLuz_solar() {
        return luz_solar;
    }

    public int getSolo_nutrientes() {
        return solo_nutrientes;
    }

    public int getSolo_salinidade() {
        return solo_salinidade;
    }

    public int getSolo_textura() {
        return solo_textura;
    }

    public int getSolo_umidade() {
        return solo_umidade;
    }

    // sets
    public void setPlantId(int plantId) {
    	this.plantId = plantId;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setNome_pop(String nome_pop) {
        this.nome_pop = nome_pop;
    }

    public void setNomecien(String nomecien) {
        this.nomecien = nomecien;
    }

    public void setImagemurl(String imagemurl) {
        this.imagemurl = imagemurl;
    }


    public void setCresc_forma(String cresc_forma) {
        this.cresc_forma = cresc_forma;
    }

    public void setCresc_taxa(String cresc_taxa) {
        this.cresc_taxa = cresc_taxa;
    }


    public void setPh_max(float ph_max) {
        this.ph_max = ph_max;
    }

    public void setPh_min(float ph_min) {
        this.ph_min = ph_min;
    }

    public void setLuz_solar(int luz_solar) {
        this.luz_solar = luz_solar;
    }

    public void setSolo_nutrientes(int solo_nutrientes) {
        this.solo_nutrientes = solo_nutrientes;
    }

    public void setSolo_salinidade(int solo_salinidade) {
        this.solo_salinidade = solo_salinidade;
    }

    public void setSolo_textura(int solo_textura) {
        this.solo_textura = solo_textura;
    }

    public void setSolo_umidade(int solo_umidade) {
        this.solo_umidade = solo_umidade;
    }

}
