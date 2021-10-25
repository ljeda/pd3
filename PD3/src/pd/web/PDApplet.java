package pd.web;

import java.util.*;

import javax.swing.*;

import pd.core.*;

/**
 * @author Łukasz Jęda
 * 
 */
public class PDApplet extends JApplet {

	public void init() {
		setSize(1000, 600);
		add(_panel);
	}

	public void encrypt() {
		_panel.clearOutputTextArea();
		Alphabet alphabet = new Alphabet(_panel.getAlphabet());
		Map<CharacterArray, Character> qMix = validateMixDefiniton(_panel.getQMix()), yMix = validateMixDefiniton(_panel.getYMix()),
				zMix = validateMixDefiniton(_panel.getZMix());
		BlackBox blackBox = new BlackBox(alphabet, qMix, yMix, zMix, _panel.getKWord(), _panel.getRWord(), _panel.getNIterationNumber());
		_panel.setCryptotext(blackBox.encrypt(_panel.getPlaintext()));
	}

	public void decrypt() {
		_panel.clearOutputTextArea();
		Alphabet alphabet = new Alphabet(_panel.getAlphabet());
		Map<CharacterArray, Character> qMix = validateMixDefiniton(_panel.getQMix()), yMix = validateMixDefiniton(_panel.getYMix()),
				zMix = validateMixDefiniton(_panel.getZMix());
		BlackBox blackBox = new BlackBox(alphabet, qMix, yMix, zMix, _panel.getKWord(), _panel.getRWord(), _panel.getNIterationNumber());
		_panel.setPlaintext(blackBox.decrypt(_panel.getCryptotext()));
	}

	private Map<CharacterArray, Character> validateMixDefiniton(String mixDefinition) {
		Map<CharacterArray, Character> mix = new HashMap<CharacterArray, Character>();
		String errorMessage = "Miks powinien być zdefiniowany poprzez trójki liter z alfabetu " + "oddzielone nową linią.";
		try {
			if (mixDefinition == null || mixDefinition.isEmpty()) {
				return null;
			}
			if (mixDefinition.length() % 4 != 3) {
				System.err.println(errorMessage);
				return null;
			}
			for (int i = 2; i < mixDefinition.length(); i = i + 4) {
				mix.put(new CharacterArray(mixDefinition.charAt(i - 2), mixDefinition.charAt(i - 1)), mixDefinition.charAt(i));
				if (mixDefinition.length() != i + 1 && mixDefinition.charAt(i + 1) != '\n') {
					System.err.println(errorMessage);
					return null;
				}
			}
			return mix;
		} catch (Exception e) {
			System.err.println(errorMessage);
			return null;
		}
	}

	private PDPanel _panel = new PDPanel(this);
	private static final long serialVersionUID = 1862220793309751722L;
}
