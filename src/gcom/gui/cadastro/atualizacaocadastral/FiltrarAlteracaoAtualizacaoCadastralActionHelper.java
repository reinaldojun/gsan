package gcom.gui.cadastro.atualizacaocadastral;

import java.util.ArrayList;
import java.util.Collection;

public class FiltrarAlteracaoAtualizacaoCadastralActionHelper {
	
	private String idEmpresa;
    
    private String idLeiturista;
    
	private String exibirCampos;
	
	private String[] colunaImoveisSelecionados;
	
	private String idLocalidadeInicial;	

	private String cdSetorComercialInicial;	
	
	private String cdRotaInicial;
	
	private String idLocalidadeFinal;
	
	private String cdSetorComercialFinal;

	private String cdRotaFinal;

	public FiltrarAlteracaoAtualizacaoCadastralActionHelper() {
	}

	public FiltrarAlteracaoAtualizacaoCadastralActionHelper(String idEmpresa,
			String idLeiturista, String exibirCampos,
			String[] colunaImoveisSelecionados, String idLocalidadeInicial,
			String cdSetorComercialInicial, String cdRotaInicial,
			String idLocalidadeFinal, String cdSetorComercialFinal,
			String cdRotaFinal) {
		this.idEmpresa = idEmpresa;
		this.idLeiturista = idLeiturista;
		this.exibirCampos = exibirCampos;
		this.colunaImoveisSelecionados = colunaImoveisSelecionados;
		this.idLocalidadeInicial = idLocalidadeInicial;
		this.cdSetorComercialInicial = cdSetorComercialInicial;
		this.cdRotaInicial = cdRotaInicial;
		this.idLocalidadeFinal = idLocalidadeFinal;
		this.cdSetorComercialFinal = cdSetorComercialFinal;
		this.cdRotaFinal = cdRotaFinal;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getIdLeiturista() {
		return idLeiturista;
	}

	public void setIdLeiturista(String idLeiturista) {
		this.idLeiturista = idLeiturista;
	}

	public String getExibirCampos() {
		return exibirCampos;
	}

	public void setExibirCampos(String exibirCampos) {
		this.exibirCampos = exibirCampos;
	}

	public Collection getColunaImoveisSelecionados() {
		if (colunaImoveisSelecionados != null && colunaImoveisSelecionados.length > 0) {
			Collection colecaoColunaImoveisSelecionados = new ArrayList();
			
			for (int i = 0; i < colunaImoveisSelecionados.length; i++) {
				colecaoColunaImoveisSelecionados.add(colunaImoveisSelecionados[i]);
			}
			
			return colecaoColunaImoveisSelecionados;
		} else {
			return null;
		}
	}

	public void setColunaImoveisSelecionados(String[] colunaImoveisSelecionados) {
		this.colunaImoveisSelecionados = colunaImoveisSelecionados;
	}

	public String getIdLocalidadeInicial() {
		return idLocalidadeInicial;
	}

	public void setIdLocalidadeInicial(String idLocalidadeInicial) {
		this.idLocalidadeInicial = idLocalidadeInicial;
	}

	public String getCdSetorComercialInicial() {
		return cdSetorComercialInicial;
	}

	public void setCdSetorComercialInicial(String cdSetorComercialInicial) {
		this.cdSetorComercialInicial = cdSetorComercialInicial;
	}

	public String getCdRotaInicial() {
		return cdRotaInicial;
	}

	public void setCdRotaInicial(String cdRotaInicial) {
		this.cdRotaInicial = cdRotaInicial;
	}

	public String getIdLocalidadeFinal() {
		return idLocalidadeFinal;
	}

	public void setIdLocalidadeFinal(String idLocalidadeFinal) {
		this.idLocalidadeFinal = idLocalidadeFinal;
	}

	public String getCdSetorComercialFinal() {
		return cdSetorComercialFinal;
	}

	public void setCdSetorComercialFinal(String cdSetorComercialFinal) {
		this.cdSetorComercialFinal = cdSetorComercialFinal;
	}

	public String getCdRotaFinal() {
		return cdRotaFinal;
	}

	public void setCdRotaFinal(String cdRotaFinal) {
		this.cdRotaFinal = cdRotaFinal;
	}
}
