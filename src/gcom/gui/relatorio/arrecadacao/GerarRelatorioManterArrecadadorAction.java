package gcom.gui.relatorio.arrecadacao;

import gcom.arrecadacao.Arrecadador;
import gcom.arrecadacao.FiltroArrecadador;
import gcom.cadastro.cliente.Cliente;
import gcom.cadastro.imovel.Imovel;
import gcom.gui.arrecadacao.FiltrarArrecadadorActionForm;
import gcom.relatorio.ExibidorProcessamentoTarefaRelatorio;
import gcom.relatorio.RelatorioVazioException;
import gcom.relatorio.arrecadacao.RelatorioManterArrecadador;
import gcom.seguranca.acesso.usuario.Usuario;
import gcom.tarefa.TarefaRelatorio;
import gcom.util.SistemaException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p>
 * Title: GCOM
 * </p>
 * <p>
 * Description: Sistema de Gest�o Comercial
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: COMPESA - Companhia Pernambucana de Saneamento
 * </p>
 * 
 * @author Fernando Fontelles
 * @version 1.0
 */

public class GerarRelatorioManterArrecadadorAction extends
		ExibidorProcessamentoTarefaRelatorio {

	/**
	 * < <Descri��o do m�todo>>
	 * 
	 * @param actionMapping
	 *            Descri��o do par�metro
	 * @param actionForm
	 *            Descri��o do par�metro
	 * @param httpServletRequest
	 *            Descri��o do par�metro
	 * @param httpServletResponse
	 *            Descri��o do par�metro
	 * @return Descri��o do retorno
	 */

	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		// cria a vari�vel de retorno
		ActionForward retorno = null;
		
		// Mudar isso quando tiver esquema de seguran�a
		HttpSession sessao = httpServletRequest.getSession(false);

		FiltrarArrecadadorActionForm filtrarArrecadadorActionForm = (FiltrarArrecadadorActionForm) actionForm;

		FiltroArrecadador filtroArrecadador = (FiltroArrecadador) sessao
				.getAttribute("filtroArrecadador");

		// Inicio da parte que vai mandar os parametros para o relat�rio

		Arrecadador arrecadadorParametros = new Arrecadador();		
		
		String id = null;		
		
		// seta os parametros que ser�o mostrados no relat�rio

		arrecadadorParametros.setId(id == null ? null : new Integer(
				id));
		
		//C�digo do Agente
		if(filtrarArrecadadorActionForm.getIdAgente() != null 
				&& !filtrarArrecadadorActionForm.getIdAgente().equals("")){
			
			arrecadadorParametros.setCodigoAgente(new Short(filtrarArrecadadorActionForm.getIdAgente()));
			
		}
		
		//Cliente
		if(filtrarArrecadadorActionForm.getNomeCliente() != null
				&& !filtrarArrecadadorActionForm.getNomeCliente().equals("")){
			
			arrecadadorParametros.setCliente(new Cliente(filtrarArrecadadorActionForm.getNomeCliente()));
			
		}
		
		//Im�vel
		if(filtrarArrecadadorActionForm.getIdImovel() != null
				&& !filtrarArrecadadorActionForm.getIdImovel().equals("")){
			
			arrecadadorParametros.setImovel(new Imovel(new Integer(filtrarArrecadadorActionForm.getIdImovel())));
			
		}
		
		//Inscri��o Estadual
		if(filtrarArrecadadorActionForm.getInscricaoEstadual() != null
				&& !filtrarArrecadadorActionForm.getInscricaoEstadual().equals("")){
			
			arrecadadorParametros.setNumeroInscricaoEstadual(filtrarArrecadadorActionForm.getInscricaoEstadual());
			
		}
		
		//Indicador de Uso
//		arrecadadorParametros.setIndicadorUso(new Short(filtrarArrecadadorActionForm.getIndicadorUso()));
		
		// Fim da parte que vai mandar os parametros para o relat�rio

		// cria uma inst�ncia da classe do relat�rio
		RelatorioManterArrecadador relatorioManterArrecadador = new RelatorioManterArrecadador(
				(Usuario)(httpServletRequest.getSession(false)).getAttribute("usuarioLogado"));
		relatorioManterArrecadador.addParametro("filtroArrecadador",
				filtroArrecadador	);
		relatorioManterArrecadador.addParametro("arrecadadorParametros",
				arrecadadorParametros);

		// chama o met�do de gerar relat�rio passando o c�digo da analise
		// como par�metro
		String tipoRelatorio = httpServletRequest.getParameter("tipoRelatorio");
		if (tipoRelatorio == null) {
			tipoRelatorio = TarefaRelatorio.TIPO_PDF + "";
		}

		relatorioManterArrecadador.addParametro("tipoFormatoRelatorio", Integer
				.parseInt(tipoRelatorio));
		try {
			retorno = processarExibicaoRelatorio(relatorioManterArrecadador,
					tipoRelatorio, httpServletRequest, httpServletResponse,
					actionMapping);

		} catch (SistemaException ex) {
			// manda o erro para a p�gina no request atual
			reportarErros(httpServletRequest, "erro.sistema");

			// seta o mapeamento de retorno para a tela de erro de popup
			retorno = actionMapping.findForward("telaErroPopup");

		} catch (RelatorioVazioException ex1) {
			// manda o erro para a p�gina no request atual
			reportarErros(httpServletRequest, "erro.relatorio.vazio");

			// seta o mapeamento de retorno para a tela de aten��o de popup
			retorno = actionMapping.findForward("telaAtencaoPopup");
		}

		// devolve o mapeamento contido na vari�vel retorno
		return retorno;
	}

}
