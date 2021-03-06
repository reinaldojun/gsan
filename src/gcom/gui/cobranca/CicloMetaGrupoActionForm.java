package gcom.gui.cobranca;

import org.apache.struts.validator.ValidatorActionForm;

/**
 * Action form responsável pelas propiedades do caso de uso de informar metas do ciclo
 * 
 * @author Francisco do Nascimento
 * @date 22/04/09
 */
public class CicloMetaGrupoActionForm extends ValidatorActionForm {
	private static final long serialVersionUID = 1L;
	
	private String idCicloMeta;
	
	private String anoMesCobranca;
	
	private String idCobrancaAcao;
	
	private String metaTotal;
	
	private String valorLimite;
	
	private String indicadorMeta;
	
	private String indicadorValor;

	public String getIndicadorMeta() {
		return indicadorMeta;
	}

	public void setIndicadorMeta(String indicadorMeta) {
		this.indicadorMeta = indicadorMeta;
	}

	public String getIndicadorValor() {
		return indicadorValor;
	}

	public void setIndicadorValor(String indicadorValor) {
		this.indicadorValor = indicadorValor;
	}

	/**
	 * @return Retorna o campo anoMesCobranca.
	 */
	public String getAnoMesCobranca() {
		return anoMesCobranca;
	}

	/**
	 * @param anoMesCobranca O anoMesCobranca a ser setado.
	 */
	public void setAnoMesCobranca(String anoMesCobranca) {
		this.anoMesCobranca = anoMesCobranca;
	}

	/**
	 * @return Retorna o campo idCobrancaAcao.
	 */
	public String getIdCobrancaAcao() {
		return idCobrancaAcao;
	}

	/**
	 * @param idCobrancaAcao O idCobrancaAcao a ser setado.
	 */
	public void setIdCobrancaAcao(String idCobrancaAcao) {
		this.idCobrancaAcao = idCobrancaAcao;
	}

	/**
	 * @return Retorna o campo metaTotal.
	 */
	public String getMetaTotal() {
		return metaTotal;
	}

	/**
	 * @param metaTotal O metaTotal a ser setado.
	 */
	public void setMetaTotal(String metaTotal) {
		this.metaTotal = metaTotal;
	}

	/**
	 * @return Retorna o campo valorLimite.
	 */
	public String getValorLimite() {
		return valorLimite;
	}

	/**
	 * @param valorLimite O valorLimite a ser setado.
	 */
	public void setValorLimite(String valorLimite) {
		this.valorLimite = valorLimite;
	}

	/**
	 * @return Retorna o campo idCicloMeta.
	 */
	public String getIdCicloMeta() {
		return idCicloMeta;
	}

	/**
	 * @param idCicloMeta O idCicloMeta a ser setado.
	 */
	public void setIdCicloMeta(String idCicloMeta) {
		this.idCicloMeta = idCicloMeta;
	}
	
	
}
