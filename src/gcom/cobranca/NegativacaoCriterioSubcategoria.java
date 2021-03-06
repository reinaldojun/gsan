package gcom.cobranca;

import gcom.cobranca.NegativacaoCriterioSubcategoriaPK;
import gcom.cadastro.imovel.Subcategoria;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class NegativacaoCriterioSubcategoria implements Serializable {
	
	private static final long serialVersionUID = 1L;

    /** identifier field */
    private NegativacaoCriterioSubcategoriaPK comp_id;

    /** persistent field */
    private Date ultimaAlteracao;

    /** nullable persistent field */
    private gcom.cobranca.NegativacaoCriterio negativacaoCriterio;

    /** nullable persistent field */
    private Subcategoria subcategoria;

    /** full constructor */
    public NegativacaoCriterioSubcategoria(NegativacaoCriterioSubcategoriaPK comp_id, Date ultimaAlteracao, gcom.cobranca.NegativacaoCriterio negativacaoCriterio, Subcategoria subcategoria) {
        this.comp_id = comp_id;
        this.ultimaAlteracao = ultimaAlteracao;
        this.negativacaoCriterio = negativacaoCriterio;
        this.subcategoria = subcategoria;
    }

    /** default constructor */
    public NegativacaoCriterioSubcategoria() {
    }

    /** minimal constructor */
    public NegativacaoCriterioSubcategoria(NegativacaoCriterioSubcategoriaPK comp_id, Date ultimaAlteracao) {
        this.comp_id = comp_id;
        this.ultimaAlteracao = ultimaAlteracao;
    }

    public NegativacaoCriterioSubcategoriaPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(NegativacaoCriterioSubcategoriaPK comp_id) {
        this.comp_id = comp_id;
    }

    public Date getUltimaAlteracao() {
        return this.ultimaAlteracao;
    }

    public void setUltimaAlteracao(Date ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
    }

    public gcom.cobranca.NegativacaoCriterio getNegativacaoCriterio() {
        return this.negativacaoCriterio;
    }

    public void setNegativacaoCriterio(gcom.cobranca.NegativacaoCriterio negativacaoCriterio) {
        this.negativacaoCriterio = negativacaoCriterio;
    }

    public Subcategoria getSubcategoria() {
        return this.subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof NegativacaoCriterioSubcategoria) ) return false;
        NegativacaoCriterioSubcategoria castOther = (NegativacaoCriterioSubcategoria) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
