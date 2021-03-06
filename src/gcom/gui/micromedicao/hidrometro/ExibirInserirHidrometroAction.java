package gcom.gui.micromedicao.hidrometro;

import gcom.fachada.Fachada;
import gcom.gui.GcomAction;
import gcom.micromedicao.hidrometro.FiltroHidrometroCapacidade;
import gcom.micromedicao.hidrometro.FiltroHidrometroClasseMetrologica;
import gcom.micromedicao.hidrometro.FiltroHidrometroDiametro;
import gcom.micromedicao.hidrometro.FiltroHidrometroLocalArmazenagem;
import gcom.micromedicao.hidrometro.FiltroHidrometroMarca;
import gcom.micromedicao.hidrometro.FiltroHidrometroRelojoaria;
import gcom.micromedicao.hidrometro.FiltroHidrometroTipo;
import gcom.micromedicao.hidrometro.HidrometroCapacidade;
import gcom.micromedicao.hidrometro.HidrometroClasseMetrologica;
import gcom.micromedicao.hidrometro.HidrometroDiametro;
import gcom.micromedicao.hidrometro.HidrometroLocalArmazenagem;
import gcom.micromedicao.hidrometro.HidrometroMarca;
import gcom.micromedicao.hidrometro.HidrometroRelojoaria;
import gcom.micromedicao.hidrometro.HidrometroTipo;
import gcom.util.ConstantesSistema;
import gcom.util.Util;
import gcom.util.filtro.ParametroSimples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * < <Descri��o da Classe>>
 * 
 * @author Administrador
 */
public class ExibirInserirHidrometroAction extends GcomAction {
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

		// Obt�m o action form
		HidrometroActionForm hidrometroActionForm = (HidrometroActionForm) actionForm;

		Collection colecaoHidrometroCapacidade = null;

		// Seta a a��o de retorno
		ActionForward retorno = actionMapping.findForward("inserirHidrometro");

		// Obt�m a sess�o
		HttpSession sessao = httpServletRequest.getSession(false);

		// Caso o form esteja na sess�o � removido
		// sessao.removeAttribute("HidrometroActionForm");

		// Obt�m a facahda
		Fachada fachada = Fachada.getInstancia();

		// Obt�m o objetoCosulta vindo na sess�o
		String objetoConsulta = (String) httpServletRequest
				.getParameter("objetoConsulta");

		// Chamou a funcionalidade pela 1� vez.
		// O usu�rio pode chamar o filtro de hidr�metro que carrega objetos na
		// sess�o
		// e depois chamar o inserir hidr�metro. Necess�rio remover os objetos
		// da sess�o deixados pelo filtro.
		String limpaSessao = (String) httpServletRequest
				.getParameter("limpaSessao");

		if (limpaSessao != null && !limpaSessao.trim().equalsIgnoreCase("")
				&& (Integer.parseInt(limpaSessao)) == 1) {

			// remove objetos da sess�o vindos do filtro
			sessao.removeAttribute("colecaoHidrometroClasseMetrologica");
			sessao.removeAttribute("colecaoHidrometroMarca");
			sessao.removeAttribute("colecaoHidrometroDiametro");
			sessao.removeAttribute("colecaoHidrometroCapacidade");
			sessao.removeAttribute("colecaoHidrometroTipo");
			sessao.removeAttribute("colecaoHidrometroRelojoaria");
		}

		
		
		
		// Verifica se o objeto � diferente de nulo
		if (objetoConsulta != null
				&& !objetoConsulta.trim().equalsIgnoreCase("")
				&& (Integer.parseInt(objetoConsulta)) == 1) {

			// Filtro para obter o local de armazenagem ativo de id informado
			FiltroHidrometroLocalArmazenagem filtroHidrometroLocalArmazenagem = new FiltroHidrometroLocalArmazenagem();

			filtroHidrometroLocalArmazenagem
					.adicionarParametro(new ParametroSimples(
							FiltroHidrometroLocalArmazenagem.ID, new Integer(
									hidrometroActionForm
											.getIdLocalArmazenagem()),
							ParametroSimples.CONECTOR_AND));
			filtroHidrometroLocalArmazenagem
					.adicionarParametro(new ParametroSimples(
							FiltroHidrometroLocalArmazenagem.INDICADOR_USO,
							ConstantesSistema.INDICADOR_USO_ATIVO));

			// Pesquisa de acordo com os par�metros informados no filtro
			Collection colecaoHidrometroLocalArmazenagem = fachada.pesquisar(
					filtroHidrometroLocalArmazenagem,
					HidrometroLocalArmazenagem.class.getName());

			// Verifica se a pesquisa retornou algum objeto para a cole��o
			if (colecaoHidrometroLocalArmazenagem != null
					&& !colecaoHidrometroLocalArmazenagem.isEmpty()) {

				// Obt�m o objeto da cole��o pesquisada
				HidrometroLocalArmazenagem hidrometroLocalArmazenagem = (HidrometroLocalArmazenagem) Util
						.retonarObjetoDeColecao(colecaoHidrometroLocalArmazenagem);

				// Exibe o c�digo e a descri��o pesquisa na p�gina
				httpServletRequest.setAttribute("corLocalArmazenagem", "valor");
				hidrometroActionForm
						.setIdLocalArmazenagem(hidrometroLocalArmazenagem
								.getId().toString());
				hidrometroActionForm
						.setLocalArmazenagemDescricao(hidrometroLocalArmazenagem
								.getDescricao());
				
			} else {

				// Exibe mensagem de c�digo inexiste e limpa o campo de c�digo
				httpServletRequest.setAttribute("corLocalArmazenagem",
						"exception");
				hidrometroActionForm.setIdLocalArmazenagem("");
				hidrometroActionForm
						.setLocalArmazenagemDescricao("Local de Armazenagem Inexistente");

			}

		} else if (sessao.getAttribute("colecaoHidrometroClasseMetrologica") == null
				&& sessao.getAttribute("colecaoHidrometroMarca") == null
				&& sessao.getAttribute("colecaoHidrometroDiametro") == null
				&& sessao.getAttribute("colecaoHidrometroCapacidade") == null
				&& sessao.getAttribute("colecaoHidrometroTipo") == null
				&& sessao.getAttribute("colecaoHidrometroRelojoaria") == null) {
			
		
			// Remove objeto da sess�o
			sessao.removeAttribute("HidrometroActionForm");

			// Filtro de hidr�metro classe metrol�gica para obter todas as
			// classes metrol�gicas ativas
			FiltroHidrometroClasseMetrologica filtroHidrometroClasseMetrologica = new FiltroHidrometroClasseMetrologica();

			filtroHidrometroClasseMetrologica
					.adicionarParametro(new ParametroSimples(
							FiltroHidrometroClasseMetrologica.INDICADOR_USO,
							ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroClasseMetrologica
					.setCampoOrderBy(FiltroHidrometroClasseMetrologica.DESCRICAO);

			// Pesquisa a cole��o de classe metrol�gica
			Collection colecaoHidrometroClasseMetrologica = fachada.pesquisar(
					filtroHidrometroClasseMetrologica,
					HidrometroClasseMetrologica.class.getName());

			// Filtro de hidr�metro marca para obter todas as marcas de
			// hidr�metros ativas
			FiltroHidrometroMarca filtroHidrometroMarca = new FiltroHidrometroMarca();

			filtroHidrometroMarca.adicionarParametro(new ParametroSimples(
					FiltroHidrometroMarca.INDICADOR_USO,
					ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroMarca
					.setCampoOrderBy(FiltroHidrometroMarca.DESCRICAO);

			// Pesquisa a cole��o de hidr�metro marca
			Collection colecaoHidrometroMarca = fachada.pesquisar(
					filtroHidrometroMarca, HidrometroMarca.class.getName());

			// Filtro de hidr�metro di�metro para obter todos os di�metros de
			// hidr�metros ativos
			FiltroHidrometroDiametro filtroHidrometroDiametro = new FiltroHidrometroDiametro();

			filtroHidrometroDiametro.adicionarParametro(new ParametroSimples(
					FiltroHidrometroDiametro.INDICADOR_USO,
					ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroDiametro
					.setCampoOrderBy(FiltroHidrometroDiametro.NUMERO_ORDEM);

			// Pesquisa a cole��o de hidr�metro capacidade
			Collection colecaoHidrometroDiametro = fachada.pesquisar(
					filtroHidrometroDiametro, HidrometroDiametro.class
							.getName());

			// Filtro de hidr�metro capacidade para obter todos as capacidade de
			// hidr�metros ativas
			FiltroHidrometroCapacidade filtroHidrometroCapacidade = new FiltroHidrometroCapacidade();

			filtroHidrometroCapacidade.adicionarParametro(new ParametroSimples(
					FiltroHidrometroCapacidade.INDICADOR_USO,
					ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroCapacidade
					.setCampoOrderBy(FiltroHidrometroCapacidade.NUMERO_ORDEM);

			// Pesquisa a cole��o de hidr�metro capacidade
			colecaoHidrometroCapacidade = fachada.pesquisar(
					filtroHidrometroCapacidade, HidrometroCapacidade.class
							.getName());

			// Filtro de hidr�metro tipo para obter todos os tipos de
			// hidr�metros ativos
			FiltroHidrometroTipo filtroHidrometroTipo = new FiltroHidrometroTipo();

			filtroHidrometroTipo.adicionarParametro(new ParametroSimples(
					FiltroHidrometroTipo.INDICADOR_USO,
					ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroTipo
					.setCampoOrderBy(FiltroHidrometroTipo.DESCRICAO);

			// Pesquisa a cole��o de hidr�metro tipo
			Collection colecaoHidrometroTipo = fachada.pesquisar(
					filtroHidrometroTipo, HidrometroTipo.class.getName());
			
			

			// Filtro de hidr�metro relojoaria para obter todos os tipos de
			// hidr�metros relojoaria ativos
			FiltroHidrometroRelojoaria filtroHidrometroRelojoaria = new FiltroHidrometroRelojoaria();

			filtroHidrometroRelojoaria.adicionarParametro(new ParametroSimples(
					FiltroHidrometroRelojoaria.INDICADOR_USO,
					ConstantesSistema.INDICADOR_USO_ATIVO));
			filtroHidrometroRelojoaria
					.setCampoOrderBy(FiltroHidrometroRelojoaria.DESCRICAO);

			// Pesquisa a cole��o de hidr�metro tipo
			Collection colecaoHidrometroRelojoaria = fachada.pesquisar(
					filtroHidrometroRelojoaria, HidrometroRelojoaria.class.getName());


			hidrometroActionForm.setIndicadorMacromedidor("" + ConstantesSistema.NUMERO_NAO_INFORMADO);

			// Envia as cole��es na sess�o
			sessao.setAttribute("colecaoHidrometroClasseMetrologica",
					colecaoHidrometroClasseMetrologica);
			sessao.setAttribute("colecaoHidrometroMarca",
					colecaoHidrometroMarca);
			sessao.setAttribute("colecaoHidrometroDiametro",
					colecaoHidrometroDiametro);
			sessao.setAttribute("colecaoHidrometroCapacidade",
					colecaoHidrometroCapacidade);
			sessao.setAttribute("colecaoHidrometroTipo", colecaoHidrometroTipo);
			sessao.setAttribute("colecaoHidrometroRelojoaria", colecaoHidrometroRelojoaria);
		}

		// Filtro de hidr�metro capacidade para obter capacidade de hidr�metro
		// de acordo com o id
		FiltroHidrometroCapacidade filtroHidrometroCapacidadeNumeroDigitos = new FiltroHidrometroCapacidade();

		// Verifica se a cole��o de hidrometro capacidade � diferente de null
		if (colecaoHidrometroCapacidade != null
				&& !colecaoHidrometroCapacidade.isEmpty()) {

			// Obt�m o primeiro objeto da collection
			Iterator colecaoHidrometroCapacidadeIterator = colecaoHidrometroCapacidade
					.iterator();
			HidrometroCapacidade hidrometroCapacidade = (HidrometroCapacidade) colecaoHidrometroCapacidadeIterator
					.next();

			// Filtra pelo primeiro objeto da collection
			filtroHidrometroCapacidadeNumeroDigitos
					.adicionarParametro(new ParametroSimples(
							FiltroHidrometroCapacidade.ID, hidrometroCapacidade
									.getId()));
		} else {
			// Filtra pelo id selecionado no combobox
			filtroHidrometroCapacidadeNumeroDigitos
					.adicionarParametro(new ParametroSimples(
							FiltroHidrometroCapacidade.ID, new Integer(
									hidrometroActionForm
											.getIdHidrometroCapacidade())));

		}

		// Pesquisa o n�mero de d�gitos de acordo com o filtro
		Collection colecaoHidrometroCapacidadeNumeroDigitos = fachada
				.pesquisar(filtroHidrometroCapacidadeNumeroDigitos,
						HidrometroCapacidade.class.getName());
		
		if(colecaoHidrometroCapacidadeNumeroDigitos != null && !colecaoHidrometroCapacidadeNumeroDigitos.isEmpty())
		{
			// Retorna o objeto pesquisado
			HidrometroCapacidade hidrometroCapacidadeNumeroDigitos = (HidrometroCapacidade) Util
					.retonarObjetoDeColecao(colecaoHidrometroCapacidadeNumeroDigitos);
			Integer leituraMinimo = 0;
			Integer leituraMaximo = 0;
			// Obt�m as leituras
			if(!hidrometroCapacidadeNumeroDigitos.getLeituraMinimo().equals(""))
			{
				leituraMinimo = new Integer(hidrometroCapacidadeNumeroDigitos
						.getLeituraMinimo().toString());
			}
			if(!hidrometroCapacidadeNumeroDigitos.getLeituraMaximo().equals(""))
			{
				leituraMaximo = new Integer(hidrometroCapacidadeNumeroDigitos
					.getLeituraMaximo().toString());
			}
			// Obt�m a quantidade da diferen�a
			int quantidade = (leituraMaximo.intValue() - leituraMinimo.intValue()) + 1;
			Collection colecaoIntervalo = new ArrayList();

			// Adiciona a quantidade da diferen�a na cole��o
			for (int i = 0; i < quantidade; i++) {

				colecaoIntervalo.add(new Integer(leituraMinimo.intValue() + i));
			}
			sessao.setAttribute("colecaoIntervalo", colecaoIntervalo);
		}
		
		return retorno;
	}
}
