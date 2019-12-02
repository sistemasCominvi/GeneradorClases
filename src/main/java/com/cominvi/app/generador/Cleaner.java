package com.cominvi.app.generador;

import java.util.Scanner;

import com.cominvi.app.generador.files.FileCreator;

public class Cleaner {
	private static Scanner scanner;

	public static void main(String[] a) {
		scanner = new Scanner(System.in);
		System.out.println("¿Estas seguro(a) de limpiar el proyecto y eliminar los archivos? S/N");
		String answer = scanner.nextLine();
		if (answer.contains("s") || answer.contains("S")) {
			FileCreator.deleteAll();
			System.out.println("Archivos eliminados exitosamente.");
		} else
			System.out.println("Acción abortada.");
	}
}
