import java.util.Scanner;

			//Alexis Aldhair Garcia Sandez
public class GatoMinimaxAlpha {
    	//Declaracion De simbolos del tablero
    private static final char VACIO = '-';
    private static final char GATO = 'X';
    private static final char RATON = 'O';
    private static final int TAMAÑO_TABLERO = 3;

    private char[][] tablero;
    private boolean turnoGato;

    public GatoMinimaxAlpha() {
        tablero = new char[TAMAÑO_TABLERO][TAMAÑO_TABLERO];
        turnoGato = true;
        reiniciarTablero();
    }

    public static void main(String[] args) {
        GatoMinimaxAlpha juego = new GatoMinimaxAlpha();
        juego.jugar();
    }

    public void reiniciarTablero() {
        for (int i = 0; i < TAMAÑO_TABLERO; i++) {
            for (int j = 0; j < TAMAÑO_TABLERO; j++) {
                tablero[i][j] = VACIO;
            }
        }
    }

    public void jugar() {
        reiniciarTablero();
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido al juego del gato y el ratón!");
        System.out.println("Gato: X  Ratón: O");
        System.out.println("El jugador gato siempre juega primero.");
        System.out.println();
        mostrarTablero();
        
        		//ciclo princippal de turnos y evaluaciones
        while (!juegoTerminado()) {
            if (turnoGato) {
                System.out.println("Turno del jugador Gato (X)");
                System.out.print("Ingrese el número de columna: ");
                int columna = scanner.nextInt() - 1;
                System.out.print("Ingrese el número de fila: ");
                int fila = scanner.nextInt() - 1;
                System.out.println();
                if (movimientoValido(fila, columna)) {
                    realizarMovimiento(fila, columna, GATO);
                    turnoGato = false;
                } else {
                    System.out.println("Movimiento inválido. Intenta de nuevo.");
                }
            } else {
                System.out.println("Turno de la IA Ratón (O)");
                int[] movimiento = minimaxAlphaBeta(0, Integer.MIN_VALUE, Integer.MAX_VALUE, RATON);
                int fila = movimiento[1];
                int columna = movimiento[2];
                realizarMovimiento(fila, columna, RATON);
                turnoGato = true;
            }

            mostrarTablero();   //ver tablero
        }
        
        //quien gano?
        char ganador = obtenerGanador();
        if (ganador == GATO) {
            System.out.println("¡El jugador Gato (X) ha ganado!");
        } else if (ganador == RATON) {
            System.out.println("¡La IA Ratón (O) ha ganado!");
        } else {
            System.out.println("¡Empate!");
        }
    }

    	//metodod minimax alphabeta y profundidad
    private int[] minimaxAlphaBeta(int profundidad, int alpha, int beta, char jugador) {
        if (jugador == GATO) {
            return maximizar(profundidad, alpha, beta);
        } else {
            return minimizar(profundidad, alpha, beta);
        }
    }
    
    	//maximizar 
    private int[] maximizar(int profundidad, int alpha, int beta) {
        int mejorPuntaje = Integer.MIN_VALUE;
        int mejorFila = -1;
        int mejorColumna = -1;

        if (juegoTerminado()) {
            int puntaje = evaluarEstado();
            return new int[] { puntaje - profundidad, mejorFila, mejorColumna };
        }

        for (int fila = 0; fila < TAMAÑO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMAÑO_TABLERO; columna++) {
                if (tablero[fila][columna] == VACIO) {
                    tablero[fila][columna] = GATO;
                    int[] resultado = minimaxAlphaBeta(profundidad + 1, alpha, beta, RATON);
                    int puntaje = resultado[0];

                    if (puntaje > mejorPuntaje) {
                        mejorPuntaje = puntaje;
                        mejorFila = fila;
                        mejorColumna = columna;
                    }

                    tablero[fila][columna] = VACIO;

                    alpha = Math.max(alpha, mejorPuntaje);
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }

        return new int[] { mejorPuntaje, mejorFila, mejorColumna };
    }
    
    											//minimizar
    private int[] minimizar(int profundidad, int alpha, int beta) {
        int mejorPuntaje = Integer.MAX_VALUE;
        int mejorFila = -1;
        int mejorColumna = -1;

        if (juegoTerminado()) {
            int puntaje = evaluarEstado();
            return new int[] { puntaje + profundidad, mejorFila, mejorColumna };
        }
        
        for (int fila = 0; fila < TAMAÑO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMAÑO_TABLERO; columna++) {
                if (tablero[fila][columna] == VACIO) {
                    tablero[fila][columna] = RATON;
                    int[] resultado = minimaxAlphaBeta(profundidad + 1, alpha, beta, GATO);
                    int puntaje = resultado[0];

                    if (puntaje < mejorPuntaje) {
                        mejorPuntaje = puntaje;
                        mejorFila = fila;
                        mejorColumna = columna;
                    }

                    tablero[fila][columna] = VACIO;

                    beta = Math.min(beta, mejorPuntaje);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return new int[] { mejorPuntaje, mejorFila, mejorColumna };
    }
    	
    			//tablero lleno es juego terminado
    private boolean juegoTerminado() {
        return obtenerGanador() != VACIO || tableroLleno();
    }

    private boolean tableroLleno() {
        for (int fila = 0; fila < TAMAÑO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMAÑO_TABLERO; columna++) {
                if (tablero[fila][columna] == VACIO) {
                    return false;
                }
            }
        }
        return true;
    }
    																//movimiento valido
    private boolean movimientoValido(int fila, int columna) {
        if (fila < 0 || fila >= TAMAÑO_TABLERO || columna < 0 || columna >= TAMAÑO_TABLERO) {
            return false;
        }

        return tablero[fila][columna] == VACIO;
    }
    														//realizar movimiento si es valido en columna y fila
    private void realizarMovimiento(int fila, int columna, char ficha) {
        tablero[fila][columna] = ficha;
    }
    																//volver a imprimir tablero
    private void mostrarTablero() {
        System.out.println("Estado del tablero:");
        for (int fila = 0; fila < TAMAÑO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMAÑO_TABLERO; columna++) {
                System.out.print(tablero[fila][columna] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    					//Metodo obtener ganador
    private char obtenerGanador() {
        // Verificar filas
        for (int fila = 0; fila < TAMAÑO_TABLERO; fila++) {
            if (tablero[fila][0] != VACIO && tablero[fila][0] == tablero[fila][1] && tablero[fila][0] == tablero[fila][2]) {
                return tablero[fila][0];
            }
        }

        // Verificar columnas
        for (int columna = 0; columna < TAMAÑO_TABLERO; columna++) {
            if (tablero[0][columna] != VACIO && tablero[0][columna] == tablero[1][columna] && tablero[0][columna] == tablero[2][columna]) {
                return tablero[0][columna];
            }
        }

        // Verificar diagonales
        if (tablero[0][0] != VACIO && tablero[0][0] == tablero[1][1] && tablero[0][0] == tablero[2][2]) {
            return tablero[0][0];
        }

        if (tablero[2][0] != VACIO && tablero[2][0] == tablero[1][1] && tablero[2][0] == tablero[0][2]) {
            return tablero[2][0];
        }

        return VACIO;
    }
    				//Evaluar Estado
    private int evaluarEstado() {
        char ganador = obtenerGanador();
        if (ganador == GATO) {
            return 10;
        } else if (ganador == RATON) {
            return -10;
        } else {
            return 0;
        }
    }
}
