package gcom.gui.seguranca.acesso;

import org.apache.struts.validator.ValidatorActionForm;

/**
 * Descri��o da classe 
 *
 * @author R�mulo Aur�lio
 * @date 10/05/2006
 */
public class PesquisarFuncionalidadeActionForm extends ValidatorActionForm{
	
	private static final long serialVersionUID = 1L;
	private String codigo;

	private String descricao;
	
	private String indicadorPontoEntrada;

	private String modulo;
	
	private String tipoPesquisa;
	
	private String indicadorRegistraTransacao;

	/**
	 * @return Retorna o campo codigo.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo O codigo a ser setado.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Retorna o campo descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao O descricao a ser setado.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return Retorna o campo indicadorPontoEntrada.
	 */
	public String getIndicadorPontoEntrada() {
		return indicadorPontoEntrada;
	}

	/**
	 * @param indicadorPontoEntrada O indicadorPontoEntrada a ser setado.
	 */
	public void setIndicadorPontoEntrada(String indicadorPontoEntrada) {
		this.indicadorPontoEntrada = indicadorPontoEntrada;
	}

	/**
	 * @return Retorna o campo modulo.
	 */
	public String getModulo() {
		return modulo;
	}

	/**
	 * @param modulo O modulo a ser setado.
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	/**
	 *@return Retorna o campo tipo de pesquisa 
	 */
	public String getTipoPesquisa() {
		return tipoPesquisa;
	}

	/**
	 * @param tipoPesquisa O tipo de pesquisa a ser setado
	 */
	public void setTipoPesquisa(String tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public String getIndicadorRegistraTransacao() {
		return indicadorRegistraTransacao;
	}

	public void setIndicadorRegistraTransacao(String indicadorRegistraTransacao) {
		this.indicadorRegistraTransacao = indicadorRegistraTransacao;
	}
	
	
	
	

}
