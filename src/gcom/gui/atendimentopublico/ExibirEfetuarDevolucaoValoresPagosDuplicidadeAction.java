package gcom.gui.atendimentopublico;

import gcom.arrecadacao.pagamento.Pagamento;
import gcom.cadastro.cliente.Cliente;
import gcom.cobranca.bean.ContaValoresHelper;
import gcom.cobranca.bean.ObterDebitoImovelOuClienteHelper;
import gcom.fachada.Fachada;
import gcom.faturamento.bean.CreditosHelper;
import gcom.faturamento.conta.Conta;
import gcom.faturamento.debito.DebitoCreditoSituacao;
import gcom.gui.ActionServletException;
import gcom.gui.GcomAction;
import gcom.util.ConstantesSistema;
import gcom.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * [UC0630]Solicitar Emiss�o do Extrato de D�bitos
 * 
 * Esta classe tem por finalidade exibir para o usu�rio a tela que exibir�
 * as contas, d�bitos, cr�ditos e guias para sele��o e posteriormente 
 * emissao do extrato de d�bito dos selecionados
 * 
 * @author Vivianne Sousa
 * @date 02/08/2007
 */
public class ExibirEfetuarDevolucaoValoresPagosDuplicidadeAction extends GcomAction {
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		ActionForward retorno = actionMapping.findForward("exibirEfetuarDevolucaoValoresPagosDuplicidade");

		FiltrarRegistroAtendimentoDevolucaoValoresActionForm form = 
		(FiltrarRegistroAtendimentoDevolucaoValoresActionForm) actionForm;

		HttpSession sessao = httpServletRequest.getSession(false);
		Fachada fachada = Fachada.getInstancia();
		
		String idImovel = form.getIdImovelSelecionado();
		httpServletRequest.setAttribute("habilitarBotaoTransferir", "2");
		
		if(httpServletRequest.getParameter("reloadPage")== null){
		
			sessao.removeAttribute("colecaoConta");
			sessao.removeAttribute("colecaoPagamento");
			sessao.removeAttribute("colecaoContaASerRetificada");
			sessao.removeAttribute("colecaoCreditoASerTransferido");
			sessao.removeAttribute("colecaoCreditoARealizar");
			
//			form.setTotalPagamentoSelecionado("0");
			httpServletRequest.removeAttribute("habilitaIncluirDebito");
			
			if (idImovel != null && !idImovel.equals("")){
				
				Cliente cliente = fachada.pesquisarClienteUsuarioImovel(new Integer(idImovel));
				form.setNomeClienteUsuarioImovelSelecionado(cliente.getNome());
				
				//CONTA
				ObterDebitoImovelOuClienteHelper obterDebitoImovelOuClienteHelper = apresentarContasImovel(new Integer(idImovel));
				sessao.setAttribute("colecaoConta", obterDebitoImovelOuClienteHelper.getColecaoContasValoresImovel());
				
				//PAGAMENTOS EM DUPLICIDADE
				Collection colecaoPagamento = fachada.pesquisaDadosRegistroAtendimentoPagamentoDuplicidade(new Integer(form.getIdRAConsulta()));
				sessao.setAttribute("colecaoPagamento", colecaoPagamento);
				
				
				
				httpServletRequest.setAttribute("habilitaIncluirDebito", 1);
				
			}
			
		}else{
//			String[] idsContas = httpServletRequest.getParameterValues("conta");
//			form.setIdsConta(Arrays.toString(idsContas).replace("[","").replace("]",""));
			if (idImovel != null && !idImovel.equals("")){
				
				Cliente cliente = fachada.pesquisarClienteUsuarioImovel(new Integer(idImovel));
				form.setNomeClienteUsuarioImovelSelecionado(cliente.getNome());
				
				//CONTA
				ObterDebitoImovelOuClienteHelper obterDebitoImovelOuClienteHelper = apresentarContasImovel(new Integer(idImovel));
				sessao.setAttribute("colecaoConta", obterDebitoImovelOuClienteHelper.getColecaoContasValoresImovel());
			}
			//bot�o calcular
			calcular(httpServletRequest, sessao);
			
		}
		
		return retorno;
	}

	/**
	 * @param httpServletRequest
	 * @param sessao
	 */
	private void calcular(HttpServletRequest httpServletRequest, HttpSession sessao) {
		
		if(httpServletRequest.getParameter("calcular") != null){
			
			BigDecimal valorCreditoSelecionado = ConstantesSistema.VALOR_ZERO;
			BigDecimal valorContaSelecionada = ConstantesSistema.VALOR_ZERO;
			
			int qtdePagamentos = 0;
			int qtdeContas = 0;
			
			String idsPagamentos = httpServletRequest.getParameter("pagamento");
			Object[] pagamentos = this.obterPagamentosSelecionadas(idsPagamentos, sessao);
			Collection colecaoPagamentos = null;
			if(pagamentos != null){
		    	colecaoPagamentos = (Collection)pagamentos[0];
		    	valorCreditoSelecionado = (BigDecimal)pagamentos[1];
				qtdePagamentos = colecaoPagamentos.size();
		    }
			sessao.setAttribute("colecaoPagamentosSelecionados",colecaoPagamentos);
			
			String idsContas = httpServletRequest.getParameter("conta");
			Object[] contas = this.obterContasSelecionadas(idsContas, sessao);
			Collection colecaoContas = null;
			if(contas != null){
		    	colecaoContas = (Collection)contas[0];
		    	valorContaSelecionada = (BigDecimal)contas[1];
		    	qtdeContas = colecaoContas.size();
		    }
			
			Iterator iteratorPagamentos = colecaoPagamentos.iterator();
			
			CreditosHelper creditoASerTransferido = null;
			Collection colecaoCreditoASerTransferido = new ArrayList();
			CreditosHelper creditoARealizar = null;
			Collection colecaoCreditoARealizar = new ArrayList();
			Collection colecaoContaASerRetificada = new ArrayList();

			///////////////////////////////////////////////////////////////////////////////
			if(qtdeContas == 1 && qtdePagamentos == 1){
				//SITUA��O 1(uma conta para um pagamento)
				
				Pagamento pagamento = (Pagamento) Util.retonarObjetoDeColecao(colecaoPagamentos);
				ContaValoresHelper helper = (ContaValoresHelper) Util.retonarObjetoDeColecao(colecaoContas);
				Conta conta = helper.getConta();
				
				if(valorCreditoSelecionado.compareTo(valorContaSelecionada) == 1){
					
					helper.setValorCreditoConta(conta.getValorTotal());
					helper.setValorAtualConta(ConstantesSistema.VALOR_ZERO);
					colecaoContaASerRetificada.add(helper);
					
					//Cr�dito Realizado
					creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),conta.getValorTotal(),conta.getId());
					colecaoCreditoASerTransferido.add(creditoASerTransferido);
					
					//Cr�dito a Realizar
					BigDecimal valorCredito = valorCreditoSelecionado.subtract(conta.getValorTotal());
					creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorCredito,null);
					colecaoCreditoARealizar.add(creditoARealizar);
					
				}else{
					
					helper.setValorCreditoConta(pagamento.getValorPagamento());
					helper.setValorAtualConta(conta.getValorTotal().subtract(valorCreditoSelecionado));
					colecaoContaASerRetificada.add(helper);
					
					//Cr�dito Realizado
					creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),conta.getId());
					colecaoCreditoASerTransferido.add(creditoASerTransferido);
				}
			}
			
			if(qtdeContas > 1 && qtdePagamentos == 1){
				//SITUA��O 2 (mais de uma conta para um pagamento)
				
				Pagamento pagamento = (Pagamento) Util.retonarObjetoDeColecao(colecaoPagamentos);
				BigDecimal saldoCredito = valorCreditoSelecionado;
				Iterator iteratorContas = colecaoContas.iterator();
				while (iteratorContas.hasNext()) {
					ContaValoresHelper helper = (ContaValoresHelper) iteratorContas.next();
					Conta conta = helper.getConta();
					
					
					if(valorCreditoSelecionado.compareTo(valorContaSelecionada) == 1){
						
						helper.setValorCreditoConta(conta.getValorTotal());
						helper.setValorAtualConta(ConstantesSistema.VALOR_ZERO);
						colecaoContaASerRetificada.add(helper);
						
						//Cr�dito Realizado
						creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),conta.getValorTotal(),conta.getId());
						colecaoCreditoASerTransferido.add(creditoASerTransferido);
						
					}else{
						
						
						if(saldoCredito.compareTo(ConstantesSistema.VALOR_ZERO) ==1 &&
								saldoCredito.compareTo(conta.getValorTotalContaBigDecimal()) == 1){
							
							helper.setValorCreditoConta(conta.getValorTotal());
							helper.setValorAtualConta(ConstantesSistema.VALOR_ZERO);
							colecaoContaASerRetificada.add(helper);
							
							//Cr�dito Realizado
							creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),conta.getValorTotal(),conta.getId());
							colecaoCreditoASerTransferido.add(creditoASerTransferido);
							
							saldoCredito = saldoCredito.subtract(conta.getValorTotal());
						}else if(saldoCredito.compareTo(ConstantesSistema.VALOR_ZERO) == 1){
							
							helper.setValorCreditoConta(saldoCredito);
							helper.setValorAtualConta(conta.getValorTotal().subtract(saldoCredito));
							colecaoContaASerRetificada.add(helper);
							
							//Cr�dito Realizado
							creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),saldoCredito,conta.getId());
							colecaoCreditoASerTransferido.add(creditoASerTransferido);
							saldoCredito = ConstantesSistema.VALOR_ZERO;
						}
						
					}
					
				}
				
				if(valorCreditoSelecionado.compareTo(valorContaSelecionada) == 1){
					//Cr�dito a Realizar
					BigDecimal valorCredito = valorCreditoSelecionado.subtract(valorContaSelecionada);
					creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorCredito,null);
					colecaoCreditoARealizar.add(creditoARealizar);
				}
				
			}
			
			if(qtdeContas == 1 && qtdePagamentos > 1){
				//SITUA��O 3 (uma conta para mais de um pagamento)
				
				ContaValoresHelper helper = (ContaValoresHelper) Util.retonarObjetoDeColecao(colecaoContas);
				Conta conta = helper.getConta();
				helper.setValorCreditoConta(ConstantesSistema.VALOR_ZERO);
				
				BigDecimal saldoCredor = valorCreditoSelecionado;
				BigDecimal valorDevolucao = conta.getValorTotalContaBigDecimal();
				
				if(saldoCredor.compareTo(valorContaSelecionada) == 1){
					
					while (iteratorPagamentos.hasNext()) {
						Pagamento pagamento = (Pagamento) iteratorPagamentos.next();
						
						if(valorDevolucao.compareTo(pagamento.getValorPagamento()) >= 0){
							
							helper.setValorCreditoConta(helper.getValorCreditoConta().add(pagamento.getValorPagamento()));
							helper.setValorAtualConta(conta.getValorTotal().subtract(helper.getValorCreditoConta()));
							if (!iteratorPagamentos.hasNext()){
								colecaoContaASerRetificada.add(helper);
							}
							
							valorDevolucao = valorDevolucao.subtract(pagamento.getValorPagamento());
							
							//Cr�dito Realizado
							creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),conta.getId());
							colecaoCreditoASerTransferido.add(creditoASerTransferido);
							
						}else{
							
							if(valorDevolucao.compareTo(ConstantesSistema.VALOR_ZERO) == 1){
								
								helper.setValorCreditoConta(helper.getValorCreditoConta().add(valorDevolucao));
								helper.setValorAtualConta(conta.getValorTotal().subtract(helper.getValorCreditoConta()));
								if (!iteratorPagamentos.hasNext()){
									colecaoContaASerRetificada.add(helper);
								}
								//Cr�dito Realizado
								creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorDevolucao,conta.getId());
								colecaoCreditoASerTransferido.add(creditoASerTransferido);
								
								//Cr�dito a Realizar
								BigDecimal valorCredito = pagamento.getValorPagamento().subtract(valorDevolucao);
								creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorCredito,null);
								colecaoCreditoARealizar.add(creditoARealizar);
								
								valorDevolucao = ConstantesSistema.VALOR_ZERO;
								
							}else if(valorDevolucao.compareTo(ConstantesSistema.VALOR_ZERO) == 0){

								//Cr�dito a Realizar
								creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),null);
								colecaoCreditoARealizar.add(creditoARealizar);
								
							}
						}
					}
					
				}else{
					
					while (iteratorPagamentos.hasNext()) {
						Pagamento pagamento = (Pagamento) iteratorPagamentos.next();
					
						helper.setValorCreditoConta(helper.getValorCreditoConta().add(pagamento.getValorPagamento()));
						helper.setValorAtualConta(conta.getValorTotal().subtract(helper.getValorCreditoConta()));
						if (!iteratorPagamentos.hasNext()){
							colecaoContaASerRetificada.add(helper);
						}
						
						//Cr�dito Realizado
						creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),conta.getId());
						colecaoCreditoASerTransferido.add(creditoASerTransferido);
						
					}
				}
			}


			if(qtdeContas >= 2 && qtdePagamentos >= 2){
				//a SITUA��O 4 (mais de uma conta para mais de um pagamento)
				
				BigDecimal valorDevolucao = ConstantesSistema.VALOR_ZERO;
				BigDecimal valorAtualConta = ConstantesSistema.VALOR_ZERO;
				Iterator iteratorContas = colecaoContas.iterator();
				ContaValoresHelper helper = null;
				int contadorContas = 0;
				boolean mudouConta = false;
				
				while (iteratorPagamentos.hasNext()) {
					
					Pagamento pagamento = (Pagamento) iteratorPagamentos.next();
					
					BigDecimal valorSaldo = pagamento.getValorPagamento();
					boolean aindaTemConta = false;
					while (iteratorContas.hasNext() && valorSaldo.compareTo(ConstantesSistema.VALOR_ZERO) == 1) {
						
						if (valorAtualConta.compareTo(ConstantesSistema.VALOR_ZERO) == 0){
							
							helper = (ContaValoresHelper) iteratorContas.next();
							contadorContas = contadorContas + 1;
							mudouConta = true;
						}
						Conta conta = helper.getConta();

						
						if(qtdeContas == contadorContas){
							aindaTemConta = false;
						}else{
							aindaTemConta = true;
						}
						
						if(helper.getValorAtualConta() == null){
							helper.setValorAtualConta(conta.getValorTotalContaBigDecimal());
						}
						
						valorDevolucao = helper.getValorAtualConta();
						
						if(valorDevolucao.compareTo(ConstantesSistema.VALOR_ZERO) == 1){
							if(valorSaldo.compareTo(helper.getValorAtualConta()) == 1){
								
								valorDevolucao = valorDevolucao.subtract(helper.getValorAtualConta());
								valorSaldo = valorSaldo.subtract(helper.getValorAtualConta());
								
								helper.setValorCreditoConta(helper.getValorCreditoConta().add(helper.getValorAtualConta()));
								helper.setValorAtualConta(conta.getValorTotalContaBigDecimal().subtract(helper.getValorCreditoConta()));
								
								if (!mudouConta){
									colecaoCreditoASerTransferido.remove(creditoASerTransferido);
								}
								creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),conta.getValorTotalContaBigDecimal(),conta.getId());
								if (mudouConta){
								colecaoContaASerRetificada.add(helper);
								
								//Cr�dito Realizado
								
								colecaoCreditoASerTransferido.add(creditoASerTransferido);
								mudouConta = false;
								} else if (!mudouConta){
									
									colecaoCreditoASerTransferido.add(creditoASerTransferido);
								}
								
								valorAtualConta = helper.getValorAtualConta();
								
							}else{
								
								valorDevolucao = valorDevolucao.subtract(valorSaldo);
								
								helper.setValorCreditoConta(helper.getValorCreditoConta().add(valorSaldo));
								helper.setValorAtualConta(helper.getValorAtualConta().subtract(valorSaldo));
								if (!mudouConta){
									colecaoCreditoASerTransferido.remove(creditoASerTransferido);
								}
								creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorSaldo,conta.getId());
								
								if (mudouConta){
									colecaoContaASerRetificada.add(helper);
//									Cr�dito Realizado
									
									colecaoCreditoASerTransferido.add(creditoASerTransferido);
									mudouConta = false;
								} else if (!mudouConta){
									
									colecaoCreditoASerTransferido.add(creditoASerTransferido);
								}
								valorSaldo = ConstantesSistema.VALOR_ZERO;
								valorAtualConta = helper.getValorAtualConta();
								
							}
						}
						
					}
					
					
					if(!aindaTemConta){
						Iterator iterContaRetificada = colecaoContaASerRetificada.iterator();
						
						while (iterContaRetificada.hasNext() && valorSaldo.compareTo(ConstantesSistema.VALOR_ZERO) == 1) {
							ContaValoresHelper helperCR = (ContaValoresHelper) iterContaRetificada.next();
							Conta conta = helperCR.getConta();

							if(helperCR.getValorAtualConta() == null ){
								helperCR.setValorAtualConta(conta.getValorTotalContaBigDecimal());
							}
							
							valorDevolucao = helperCR.getValorAtualConta();
							
							if(valorDevolucao.compareTo(ConstantesSistema.VALOR_ZERO) == 1){
								if(valorSaldo.compareTo(helperCR.getValorAtualConta()) == 1){
									
									valorDevolucao = valorDevolucao.subtract(helperCR.getValorAtualConta());
									valorSaldo = valorSaldo.subtract(helperCR.getValorAtualConta());
									
									helperCR.setValorCreditoConta(helperCR.getValorCreditoConta().add(helperCR.getValorAtualConta()));
									
									//Cr�dito Realizado
									creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),helperCR.getValorAtualConta(),conta.getId());
									colecaoCreditoASerTransferido.add(creditoASerTransferido);
	
									helperCR.setValorAtualConta(conta.getValorTotal().subtract(helperCR.getValorCreditoConta()));
									
								}else{
									
									valorDevolucao = valorDevolucao.subtract(valorSaldo);
									
									helperCR.setValorCreditoConta(helperCR.getValorCreditoConta().add(valorSaldo));
									helperCR.setValorAtualConta(helperCR.getValorAtualConta().subtract(valorSaldo));
									
									//Cr�dito Realizado
									creditoASerTransferido = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorSaldo,conta.getId());
									colecaoCreditoASerTransferido.add(creditoASerTransferido);
									
									valorSaldo = ConstantesSistema.VALOR_ZERO;
									
								}
							}
							
						}
					}
					
					if(valorSaldo.compareTo(ConstantesSistema.VALOR_ZERO) == 1){
						//Cr�dito a Realizar
						creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),valorSaldo,null);
						colecaoCreditoARealizar.add(creditoARealizar);
					}
					
				}
					
				
			}
			
			if(qtdeContas == 0 && qtdePagamentos == 1){
				//SITUA��O 5 (um pagamento selecionado sem conta selecionada)
				Pagamento pagamento = (Pagamento) Util.retonarObjetoDeColecao(colecaoPagamentos);

				//Cr�dito a Realizar
				creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),null);
				colecaoCreditoARealizar.add(creditoARealizar);
			}
			
			if(qtdeContas == 0 && qtdePagamentos > 1){
				//SITUA��O 6 (mais de um pagamento selecionado sem conta selecionada)
				
				while (iteratorPagamentos.hasNext()) {
					Pagamento pagamento = (Pagamento) iteratorPagamentos.next();
					
					//Cr�dito a Realizar
					creditoARealizar = montarObjetoCreditosHelper(pagamento.getAnoMesReferenciaPagamento(),pagamento.getValorPagamento(),null);
					colecaoCreditoARealizar.add(creditoARealizar);
					
				}
				
			}
			
			sessao.setAttribute("colecaoContaASerRetificada",colecaoContaASerRetificada);
			sessao.setAttribute("colecaoCreditoASerTransferido",colecaoCreditoASerTransferido);
			sessao.setAttribute("colecaoCreditoARealizar",colecaoCreditoARealizar);
		
			httpServletRequest.setAttribute("habilitarBotaoTransferir", "1");
			
		}
	}
	
	private Object[] obterContasSelecionadas(String idsContas, HttpSession sessao){
		
		Object[] retorno = null;
		Collection<ContaValoresHelper> colecaoContas = null;
		BigDecimal valorTotalConta = BigDecimal.ZERO;

		if (idsContas != null && !idsContas.equals("")){
			retorno = new Object[6];
			colecaoContas = new ArrayList();
			
			Collection colecaoContasSessao = (Collection) sessao.getAttribute("colecaoConta");
			Iterator itColecaoContasSessao = colecaoContasSessao.iterator();
			ContaValoresHelper contaValoresHelper = null;
			
			String[] idsContasArray = idsContas.split(",");
			
			while (itColecaoContasSessao.hasNext()){
				
				contaValoresHelper = (ContaValoresHelper) itColecaoContasSessao.next();
				contaValoresHelper.setValorCreditoConta(ConstantesSistema.VALOR_ZERO);
				for(int x=0; x<idsContasArray.length; x++){
					
					if (contaValoresHelper.getConta().getId().equals(new Integer(idsContasArray[x]))){
						colecaoContas.add(contaValoresHelper);
						valorTotalConta = valorTotalConta.add(contaValoresHelper.getValorTotalConta());
						
					}
				}
			}
			
			
			retorno[0] = colecaoContas;
			retorno[1] = valorTotalConta;

		}

		return retorno;
	}
	
	
	private Object[] obterPagamentosSelecionadas(String idsPagamentos, HttpSession sessao){
		
		Object[] retorno = null;
		Collection<Pagamento> colecaoPagamentos = null;
		BigDecimal valorTotalPagamentos = BigDecimal.ZERO;
		
		if (idsPagamentos != null && !idsPagamentos.equals("")){
			retorno = new Object[2];
			colecaoPagamentos = new ArrayList();
			
			Collection colecaoPagamentosSessao = (Collection) sessao.getAttribute("colecaoPagamento");
			Iterator itColecaoPagamentosSessao = colecaoPagamentosSessao.iterator();
			Pagamento pagamento = null;
			
			String[] idsDebitosArray = idsPagamentos.split(",");
			
			while (itColecaoPagamentosSessao.hasNext()){
				
				pagamento = (Pagamento) itColecaoPagamentosSessao.next();
				
				for(int x=0; x<idsDebitosArray.length; x++){
					
					if (pagamento.getId().equals(new Integer(idsDebitosArray[x]))){
						colecaoPagamentos.add(pagamento);
						valorTotalPagamentos = valorTotalPagamentos.add(pagamento.getValorPagamento());
						
					}
				}
			}
			
			Collections.sort((List) colecaoPagamentos, new Comparator() {
    			public int compare(Object a, Object b) {
    			BigDecimal valor1 = ((Pagamento)a).getValorPagamento();
    			BigDecimal valor2 = ((Pagamento)b).getValorPagamento();
    			
    			return valor2.compareTo(valor1);
    			
    			}
    		});
			
			
			retorno[0] = colecaoPagamentos;
			retorno[1] = valorTotalPagamentos;
		}else{
			throw new ActionServletException("atencao.nenhum.pagamento.selecionado");
		}

		return retorno;
	}
	
	/**
	 * retira contas parceladas ou em revis�o da cole��o de contas em d�bito
	 * 
	 * @author Vivianne Sousa
	 * @created 16/03/2011
	 */
	private ObterDebitoImovelOuClienteHelper apresentarContasImovel(Integer idImovel) {

		Date dataVencimentoInicial = Util.criarData(1, 1, 0001);
		Date dataVencimentoFinal = Util.criarData(31, 12, 9999);

		// [UC0067] Obter D�bito do Im�vel ou Cliente
		ObterDebitoImovelOuClienteHelper imovelDebitoCredito = Fachada.getInstancia()
				.obterDebitoImovelOuCliente(1, // indicadorDebito
						idImovel.toString(), // idImovel
						null, // codigoCliente
						null, // clienteRelacaoTipo
						"000101", // anoMesInicialReferenciaDebito
						"999912", // anoMesFinalReferenciaDebito
						dataVencimentoInicial, // anoMesInicialVencimentoDebito
						dataVencimentoFinal, // anoMesFinalVencimentoDebito
						1, // indicadorPagamento
						1, // indicadorConta
						2, // indicadorDebitoACobrar
						2, // indicadorCreditoARealizar
						2, // indicadorNotasPromissorias
						2, // indicadorGuiasPagamento
						2, // indicadorCalcularAcrescimoImpontualidade
						true);// indicadorContas

		// CONTA
		if (imovelDebitoCredito.getColecaoContasValoresImovel() != null
				&& !imovelDebitoCredito.getColecaoContasValoresImovel().isEmpty()) {

			Collection<ContaValoresHelper> colecaoContaValoresNaoParcelamento = new ArrayList();

			// Selecionar apenas as contas que n�o estejam parceladas
			Iterator itColecaoConta = imovelDebitoCredito.getColecaoContasValoresImovel().iterator();

			while (itColecaoConta.hasNext()) {

				ContaValoresHelper contaValoresHelper = (ContaValoresHelper) itColecaoConta.next();
				contaValoresHelper.setValorCreditoConta(ConstantesSistema.VALOR_ZERO);
				if (!contaValoresHelper.getConta().getDebitoCreditoSituacaoAtual().getId().equals(DebitoCreditoSituacao.PARCELADA)
						&& contaValoresHelper.getConta().getContaMotivoRevisao() == null) {

					colecaoContaValoresNaoParcelamento.add(contaValoresHelper);
				}
			}

			imovelDebitoCredito.setColecaoContasValoresImovel(colecaoContaValoresNaoParcelamento);

		}

		return imovelDebitoCredito;

	}
	
	private CreditosHelper montarObjetoCreditosHelper(Integer referenciaCredito, BigDecimal valorCredito,Integer idContaCreditorealizado){
		
		CreditosHelper helper = new CreditosHelper();
		helper.setOrigemCredito("DOCTOS PAGOS EM DUPLICIDADE/EXCESSO");
		helper.setTipoCredito("DEV.PAGTOS DUPL.");
		helper.setReferenciaCredito(referenciaCredito);
		helper.setValorCredito(valorCredito);
		if(idContaCreditorealizado != null){
			helper.setIdContaCreditorealizado(idContaCreditorealizado);
		}
	
		return helper;
	}
}
