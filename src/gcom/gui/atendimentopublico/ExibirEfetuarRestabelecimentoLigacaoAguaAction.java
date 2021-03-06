package gcom.gui.atendimentopublico;

import gcom.atendimentopublico.ordemservico.FiltroServicoNaoCobrancaMotivo;
import gcom.atendimentopublico.ordemservico.OrdemServico;
import gcom.atendimentopublico.ordemservico.ServicoNaoCobrancaMotivo;
import gcom.atendimentopublico.ordemservico.ServicoTipo;
import gcom.cadastro.cliente.Cliente;
import gcom.cadastro.cliente.ClienteImovel;
import gcom.cadastro.cliente.ClienteRelacaoTipo;
import gcom.cadastro.cliente.FiltroClienteImovel;
import gcom.cadastro.imovel.Imovel;
import gcom.cadastro.sistemaparametro.SistemaParametro;
import gcom.fachada.Fachada;
import gcom.faturamento.debito.FiltroDebitoTipo;
import gcom.gui.ActionServletException;
import gcom.gui.GcomAction;
import gcom.micromedicao.consumo.LigacaoTipo;
import gcom.seguranca.acesso.usuario.Usuario;
import gcom.util.ConstantesSistema;
import gcom.util.Util;
import gcom.util.filtro.ParametroNulo;
import gcom.util.filtro.ParametroSimples;

import java.math.BigDecimal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Descri��o da classe
 * 
 * @author R�mulo Aur�lio
 * @date 12/07/2006
 */
public class ExibirEfetuarRestabelecimentoLigacaoAguaAction extends GcomAction {
	/**
	 * [UC0359] Efetuar Restabelecimento Liga��o de �gua
	 * 
	 * Este caso de uso permite efetuar o restabelecimento da liga��o de �gua,
	 * sendo chamada pela funcionalidade que encerra a execu��o da ordem de
	 * servi�o ou chamada diretamente do menu.
	 * 
	 * @param actionMapping
	 *            Description of the Parameter
	 * @param actionForm
	 *            Description of the Parameter
	 * @param httpServletRequest
	 *            Description of the Parameter
	 * @param httpServletResponse
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		// Set no mapeamento de retorno
		ActionForward retorno = actionMapping.findForward("efetuarRestabelecimentoLigacaoAgua");

		EfetuarRestabelecimentoLigacaoAguaActionForm efetuarRestabelecimentoLigacaoAguaActionForm = 
			(EfetuarRestabelecimentoLigacaoAguaActionForm) actionForm;

		Fachada fachada = Fachada.getInstancia();

		Boolean veioEncerrarOS = null;
		if (httpServletRequest.getAttribute("veioEncerrarOS") != null) {
			veioEncerrarOS = Boolean.TRUE;
		} else {
			if (efetuarRestabelecimentoLigacaoAguaActionForm.getVeioEncerrarOS() != null
					&& !efetuarRestabelecimentoLigacaoAguaActionForm
							.getVeioEncerrarOS().equals("")) {
				if (efetuarRestabelecimentoLigacaoAguaActionForm.getVeioEncerrarOS()
						.toLowerCase().equals("true")) {
					veioEncerrarOS = veioEncerrarOS = Boolean.TRUE;
				} else {
					veioEncerrarOS = veioEncerrarOS = Boolean.FALSE;
				}
			} else {
				veioEncerrarOS = Boolean.FALSE;
			}
		}

		HttpSession sessao = httpServletRequest.getSession(false);
		
		Usuario usuarioLogado = (Usuario) sessao.getAttribute("usuarioLogado");

		String idOrdemServico = null;

		if (efetuarRestabelecimentoLigacaoAguaActionForm.getIdOrdemServico() != null) {
			idOrdemServico = efetuarRestabelecimentoLigacaoAguaActionForm
					.getIdOrdemServico();
		} else {
			idOrdemServico = (String) httpServletRequest.getAttribute("veioEncerrarOS");
			efetuarRestabelecimentoLigacaoAguaActionForm
					.setDataRestabelecimento((String) httpServletRequest
							.getAttribute("dataEncerramento"));

			sessao.setAttribute("caminhoRetornoIntegracaoComercial",
					httpServletRequest
							.getAttribute("caminhoRetornoIntegracaoComercial"));
		}

		OrdemServico ordemServico = null;

		if (idOrdemServico != null && !idOrdemServico.trim().equals("")) {

			ordemServico = fachada.recuperaOSPorId(new Integer(idOrdemServico));

			if (ordemServico != null) {

				fachada.validarExibirRestabelecimentoLigacaoAgua(ordemServico,
						veioEncerrarOS);

				sessao.setAttribute("ordemServico", ordemServico);

				// Filtro para o campo Tpo Debito
				Collection colecaoNaoCobranca = (Collection) sessao
						.getAttribute("colecaoNaoCobranca");
				if (colecaoNaoCobranca == null) {
					FiltroServicoNaoCobrancaMotivo filtroServicoNaoCobrancaMotivo = new FiltroServicoNaoCobrancaMotivo();

					filtroServicoNaoCobrancaMotivo
							.setCampoOrderBy(FiltroDebitoTipo.DESCRICAO);

					colecaoNaoCobranca = fachada.pesquisar(
							filtroServicoNaoCobrancaMotivo,
							ServicoNaoCobrancaMotivo.class.getName());

					if (colecaoNaoCobranca != null
							&& !colecaoNaoCobranca.isEmpty()) {
						sessao.setAttribute("colecaoNaoCobranca",
								colecaoNaoCobranca);
					} else {
						throw new ActionServletException(
								"atencao.naocadastrado", null,
								"Motivo da N�o Cobran�a");
					}
				}

				efetuarRestabelecimentoLigacaoAguaActionForm
						.setIdOrdemServico(idOrdemServico);

				efetuarRestabelecimentoLigacaoAguaActionForm
						.setVeioEncerrarOS("" + veioEncerrarOS);
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setNomeOrdemServico(ordemServico.getServicoTipo()
								.getDescricao());

				Imovel imovel = ordemServico.getRegistroAtendimento()
						.getImovel();
				String idImovel = imovel.getId().toString();

				sessao.setAttribute("idImovel", idImovel);

				// Matricula do Imovel
				String matriculaImovel = imovel.getId().toString();

				efetuarRestabelecimentoLigacaoAguaActionForm
						.setMatriculaImovel("" + matriculaImovel);

				// Inscri��o do Imov�l
				String inscricaoImovel = fachada
						.pesquisarInscricaoImovel(imovel.getId());

				efetuarRestabelecimentoLigacaoAguaActionForm
						.setMatriculaImovel(matriculaImovel);
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setInscricaoImovel(inscricaoImovel);

				// Cliente Imovel
				this
						.pesquisarCliente(efetuarRestabelecimentoLigacaoAguaActionForm);

				// Situa��o da Liga��o de Agua
				String situacaoLigacaoAgua = imovel.getLigacaoAguaSituacao()
						.getDescricao();
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setSituacaoLigacaoAgua(situacaoLigacaoAgua);

				// Situa��o da Liga��o de Esgoto
				String situacaoLigacaoEsgoto = imovel
						.getLigacaoEsgotoSituacao().getDescricao();
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setSituacaoLigacaoEsgoto(situacaoLigacaoEsgoto);

				BigDecimal valorDebito = new BigDecimal(0);
				
				if (ordemServico.getServicoTipo() != null
						&& ordemServico.getServicoTipo().getDebitoTipo() != null) {
					efetuarRestabelecimentoLigacaoAguaActionForm
							.setIdTipoDebito(ordemServico.getServicoTipo()
									.getDebitoTipo().getId().toString());
					efetuarRestabelecimentoLigacaoAguaActionForm
							.setDescricaoTipoDebito(ordemServico
									.getServicoTipo().getDebitoTipo()
									.getDescricao());
					
					//[FS0013] - Altera��o de Valor
					this.permitirAlteracaoValor(ordemServico.getServicoTipo(), efetuarRestabelecimentoLigacaoAguaActionForm);
					String calculaValores = httpServletRequest.getParameter("calculaValores");
					
					
					SistemaParametro sistemaParametro = this.getFachada().pesquisarParametrosDoSistema();
					Integer qtdeParcelas = null;
					
					if(calculaValores != null && calculaValores.equals("S")){
						
						//[UC0186] - Calcular Presta��o
						BigDecimal  taxaJurosFinanciamento = null; 
						qtdeParcelas = new Integer(efetuarRestabelecimentoLigacaoAguaActionForm.getQuantidadeParcelas());
						
						if(ordemServico.getServicoTipo().getIndicadorCobrarJuros() == ConstantesSistema.SIM.shortValue() && 
							qtdeParcelas.intValue() != 1){
							
							taxaJurosFinanciamento = sistemaParametro.getPercentualTaxaJurosFinanciamento();
						}else{
							taxaJurosFinanciamento = new BigDecimal(0);
						}
						
						BigDecimal valorPrestacao = null;
						if(taxaJurosFinanciamento != null){
							
							valorDebito = new BigDecimal(efetuarRestabelecimentoLigacaoAguaActionForm.getValorDebito().replace(",","."));
							
							String percentualCobranca = efetuarRestabelecimentoLigacaoAguaActionForm.getPercentualCobranca();
							
							if(percentualCobranca.equals("70")){
								valorDebito = valorDebito.multiply(new BigDecimal(0.7));
							}else if (percentualCobranca.equals("50")){
								valorDebito = valorDebito.multiply(new BigDecimal(0.5));
							}
							
							valorPrestacao =
								this.getFachada().calcularPrestacao(
									taxaJurosFinanciamento,
									qtdeParcelas, 
									valorDebito, 
									new BigDecimal("0.00"));
							
							valorPrestacao.setScale(2,BigDecimal.ROUND_HALF_UP);
						}
						
						if (valorPrestacao != null) {
							String valorPrestacaoComVirgula = Util.formataBigDecimal(valorPrestacao,2,true);
							efetuarRestabelecimentoLigacaoAguaActionForm.setValorParcelas(valorPrestacaoComVirgula);
						} else {
							efetuarRestabelecimentoLigacaoAguaActionForm.setValorParcelas("0,00");
						}						
						
					}else{
						valorDebito = fachada.obterValorDebito(
								ordemServico.getServicoTipo().getId(), imovel
										.getId(), new Short(
										LigacaoTipo.LIGACAO_AGUA + ""));
						efetuarRestabelecimentoLigacaoAguaActionForm
								.setValorDebito(Util.formataBigDecimal(valorDebito,
										2, true));
					}

					if (ordemServico.getServicoNaoCobrancaMotivo() != null) {
						efetuarRestabelecimentoLigacaoAguaActionForm
								.setMotivoNaoCobranca(ordemServico
										.getServicoNaoCobrancaMotivo().getId()
										.toString());
					}

					if (ordemServico.getPercentualCobranca() != null) {
						efetuarRestabelecimentoLigacaoAguaActionForm
								.setPercentualCobranca(ordemServico
										.getPercentualCobranca().toString());
					}
				}

				// Data de Restabelecimento
				String dataEncerramentoOdServico = Util
						.formatarData(ordemServico.getDataEncerramento());
				if (dataEncerramentoOdServico != null
						&& !dataEncerramentoOdServico.equals("")) {
					efetuarRestabelecimentoLigacaoAguaActionForm
							.setDataRestabelecimento(dataEncerramentoOdServico);
				}

				// -----------------------------------------------------------
				// Verificar permiss�o especial
				boolean temPermissaoMotivoNaoCobranca = fachada.verificarPermissaoInformarMotivoNaoCobranca(usuarioLogado);
				// -----------------------------------------------------------
				
				if (temPermissaoMotivoNaoCobranca) {
					httpServletRequest.setAttribute("permissaoMotivoNaoCobranca", temPermissaoMotivoNaoCobranca);
				}else{
					efetuarRestabelecimentoLigacaoAguaActionForm.setPercentualCobranca("100");
					efetuarRestabelecimentoLigacaoAguaActionForm.setQuantidadeParcelas("1");
					efetuarRestabelecimentoLigacaoAguaActionForm.setValorParcelas(Util.formataBigDecimal(valorDebito,2,true));
				}
			} else {
				httpServletRequest.setAttribute("nomeCampo", "idOrdemServico");
				httpServletRequest.setAttribute("OrdemServicoInexistente", true);
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setIdOrdemServico("");
				efetuarRestabelecimentoLigacaoAguaActionForm
						.setNomeOrdemServico("ORDEM DE SERVI�O INEXISTENTE");
			}
		}

		return retorno;
	}
	
	
	/*
	 * [FS0013 - Altera��o de Valor]
	 * 
	 * autor: Raphael Rossiter
	 * data: 19/04/2007
	 */
	private void permitirAlteracaoValor(ServicoTipo servicoTipo, EfetuarRestabelecimentoLigacaoAguaActionForm form){
		
		if (servicoTipo.getIndicadorPermiteAlterarValor() == 
			ConstantesSistema.INDICADOR_USO_ATIVO.shortValue()){
			
			form.setAlteracaoValor("OK");
		}
		else{
			form.setAlteracaoValor("");
		}
		
	}

	/**
	 * Pesquisa Cliente
	 * 
	 * @author Rafael Pinto
	 * @date 25/08/2006
	 */
	private void pesquisarCliente(
			EfetuarRestabelecimentoLigacaoAguaActionForm efetuarRestabelecimentoLigacaoAguaActionForm) {

		// Filtro para carregar o Cliente
		FiltroClienteImovel filtroClienteImovel = new FiltroClienteImovel();

		filtroClienteImovel.adicionarParametro(new ParametroSimples(
				FiltroClienteImovel.IMOVEL_ID,
				efetuarRestabelecimentoLigacaoAguaActionForm
						.getMatriculaImovel()));

		filtroClienteImovel.adicionarParametro(new ParametroSimples(
				FiltroClienteImovel.CLIENTE_RELACAO_TIPO,
				ClienteRelacaoTipo.USUARIO));

		filtroClienteImovel.adicionarParametro(new ParametroNulo(
				FiltroClienteImovel.DATA_FIM_RELACAO));

		filtroClienteImovel.adicionarCaminhoParaCarregamentoEntidade("cliente");

		Collection colecaoClienteImovel = Fachada.getInstancia().pesquisar(
				filtroClienteImovel, ClienteImovel.class.getName());

		if (colecaoClienteImovel != null && !colecaoClienteImovel.isEmpty()) {

			ClienteImovel clienteImovel = (ClienteImovel) colecaoClienteImovel
					.iterator().next();

			Cliente cliente = clienteImovel.getCliente();

			String documento = "";

			if (cliente.getCpf() != null && !cliente.getCpf().equals("")) {
				documento = cliente.getCpfFormatado();
			} else {
				documento = cliente.getCnpjFormatado();
			}
			// Cliente Nome/CPF-CNPJ
			efetuarRestabelecimentoLigacaoAguaActionForm
					.setClienteUsuario(cliente.getNome());
			efetuarRestabelecimentoLigacaoAguaActionForm
					.setCpfCnpjCliente(documento);

		} else {
			throw new ActionServletException("atencao.naocadastrado", null,
					"Cliente");
		}
	}

}
