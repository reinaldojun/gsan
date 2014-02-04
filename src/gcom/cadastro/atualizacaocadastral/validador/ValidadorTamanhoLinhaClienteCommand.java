package gcom.cadastro.atualizacaocadastral.validador;

import gcom.cadastro.atualizacaocadastral.command.AtualizacaoCadastralImovel;
import gcom.util.ParserUtil;

import java.util.Map;

public class ValidadorTamanhoLinhaClienteCommand extends ValidadorCommand{
	private ParserUtil parser;

	public ValidadorTamanhoLinhaClienteCommand(ParserUtil parser, AtualizacaoCadastralImovel cadastroImovel) {
		super(cadastroImovel, null);
		this.parser = parser;
	}

	@Override
	public void execute() {
		if (parser.getFonte().length() != 742){
			cadastroImovel.addMensagemErro("A linha Tipo 01 n�o est� compat�vel ao definido no leiaute");
		}
	}
}
