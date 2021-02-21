package com.GenZVirus.SoulmateMod.File;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.GenZVirus.SoulmateMod.Database.Database;
import com.GenZVirus.SoulmateMod.Util.Soulmates;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.dedicated.DedicatedServer;

public class XMLFileJava {

	public static final String default_player_uuid = "00000000-0000-0000-0000-000000000000";

	public static String default_xmlFilePath = "Soulmate/";

	public XMLFileJava(ServerPlayerEntity player) {

		try {

			String xmlFilePath = default_xmlFilePath + player.getUniqueID().toString() + ".xml";
			File file = new File(xmlFilePath);
			File parent = file.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) { throw new IllegalStateException("Couldn't create dir: " + parent); }

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			// Root
			Element root = document.createElement("Root");
			document.appendChild(root);

			// Enabled or Disabled
			Element player_uuid = document.createElement("Soulmate");
			player_uuid.appendChild(document.createTextNode(default_player_uuid));
			root.appendChild(player_uuid);

			// create the xml file
			// transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));

			// If you use
			// StreamResult result = new StreamResult(System.out);
			// the output will be pushed to the standard output ...
			// You can use that for debugging

			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void editElement(String elementTag, String elementTextContent, UUID player) {
		try {
			String xmlFilePath = default_xmlFilePath + player.toString() + ".xml";
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFilePath);

			// Get root element

			checkFileElement(document, xmlFilePath, elementTag, player);
			Node element = document.getElementsByTagName(elementTag).item(0);
			element.setTextContent(elementTextContent);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	public static String readElement(String elementTag, UUID player) {
		try {
			String xmlFilePath = default_xmlFilePath + player.toString() + ".xml";
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFilePath);
			checkFileElement(document, xmlFilePath, elementTag, player);
			Node element = document.getElementsByTagName(elementTag).item(0);
			return element.getTextContent();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		return "0";
	}

	public static void checkFileElement(Document document, String xmlFilePath, String elementTag, UUID player) {
		Node element = document.getElementsByTagName(elementTag).item(0);
		if (element == null) {
			try {
				element = document.createElement(elementTag);
				element.appendChild(document.createTextNode(Integer.toString(0)));
				Node root = document.getElementsByTagName("Root").item(0);
				root.appendChild(element);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(xmlFilePath));
				transformer.transform(domSource, streamResult);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
			resetElement(elementTag, player);
		}
	}

	public static void checkFileAndMake(ServerPlayerEntity player) {
		if (!(player.getServer() instanceof DedicatedServer)) {
			IntegratedServerHelper.setIntegratedServerAddress();
		} else {
			default_xmlFilePath = "Soulmate/";
		}
		String xmlFilePath = default_xmlFilePath + player.getUniqueID().toString() + ".xml";
		File file = new File(xmlFilePath);
		boolean found = file.exists();

		if (!found) {
			new XMLFileJava(player);
		}

	}

	public static void resetToDefault(UUID player) {
		XMLFileJava.editElement("Soulmate", default_player_uuid, player);
	}

	private static void resetElement(String elementTag, UUID player) {
		if (elementTag.equals("Soulmate")) {
			XMLFileJava.editElement("Soulmate", default_player_uuid, player);
		}

	}

	public static void load(ServerPlayerEntity player) {
		checkFileAndMake(player);
		UUID player1 = player.getUniqueID();
		UUID player2 = UUID.fromString(XMLFileJava.readElement("Soulmate", player.getUniqueID()));
		
		if (player2.toString().contentEquals(default_player_uuid)) { return; }
		if (Database.hasPlayerInAGroup(player1)) { return; }
		Database.LIST.add(new Soulmates(player1, player2));
	}

	public static void save(ServerPlayerEntity player) {
		checkFileAndMake(player);
		UUID uuid = player.getUniqueID();
		Database.savePlayer(uuid);
	}

}
