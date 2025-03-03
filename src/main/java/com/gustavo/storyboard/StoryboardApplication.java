package com.gustavo.storyboard;

import com.gustavo.storyboard.model.*;
import com.gustavo.storyboard.repository.*;
import com.gustavo.storyboard.service.BlockedCardHistoryService;
import com.gustavo.storyboard.service.CardMovementHistoryService;
import com.gustavo.storyboard.service.CardService;
import com.gustavo.storyboard.service.StoryBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class StoryboardApplication implements CommandLineRunner {
	@Autowired
	private StoryBoardService boardService;
	@Autowired
	private CardService cardService;
	@Autowired
	private StoryBoardRepository boardRepository;
	@Autowired
	private CardMovementHistoryService movementHistoryService;
	@Autowired
	private CardMovementHistoryRepository movementHistoryRepository;
	@Autowired
	private BoardColumnRepository boardColumnRepository;
	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private BlockedCardHistoryRepository blockedCardHistoryRepository;
	@Autowired
	private BlockedCardHistoryService blockedCardHistoryService;
	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		SpringApplication.run(com.gustavo.storyboard.StoryboardApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			mainMenu();
		} catch (Exception e) {
			System.err.println("Erro fatal: " + e.getMessage());
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	private void mainMenu() {
		while (true) {
			try {
				System.out.println("\nMenu Principal:");
				System.out.println("1. Criar novo story board");
				System.out.println("2. Selecionar story board existente");
				System.out.println("3. Excluir story boards");
				System.out.println("4. Sair");

				if (!scanner.hasNextInt()) {
					System.out.println("Entrada inválida! Digite um número.");
					scanner.next();
					continue;
				}

				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
					case 1 -> createNewBoard();
					case 2 -> selectBoard();
					case 3 -> deleteBoard();
					case 4 -> System.exit(0);
					default -> System.out.println("Opção inválida!");
				}
			} catch (InputMismatchException e) {
				System.out.println("Erro: Entrada inválida. Digite apenas números.");
				scanner.nextLine();
			} catch (Exception e) {
				System.out.println("Erro inesperado: " + e.getMessage());
			}
		}
	}

	private void createNewBoard() {
		try {
			System.out.println("Nome do novo story board:");
			String name = scanner.nextLine();

			if (name == null || name.trim().isEmpty()) {
				System.out.println("Erro: Nome não pode ser vazio!");
				return;
			}

			boardService.createStoryBoard(name);
			System.out.println("Story board criado!");
		} catch (DataIntegrityViolationException e) {
			System.out.println("Erro: Já existe um story board com este nome!");
		} catch (Exception e) {
			System.out.println("Erro ao criar board: " + e.getMessage());
		}
	}

	private void selectBoard() {
		try {
			System.out.println("Selecione um story board:");
			List<StoryBoard> boards = boardRepository.findAll();

			if (boards.isEmpty()) {
				System.out.println("Nenhum story board disponível!");
				return;
			}

			boards.forEach(b -> System.out.println(b.getId() + ": " + b.getName()));

			if (!scanner.hasNextLong()) {
				System.out.println("ID inválido!");
				scanner.nextLine();
				return;
			}

			Long id = scanner.nextLong();
			scanner.nextLine();

			if (!boardRepository.existsById(id)) {
				System.out.println("ID não encontrado!");
				return;
			}

			boardManagementMenu(id);
		} catch (Exception e) {
			System.out.println("Erro ao selecionar board: " + e.getMessage());
		}
	}

	private void deleteBoard() {
		try {
			System.out.println("\n=== Excluir Story Boards ===");
			List<StoryBoard> boards = boardRepository.findAll();

			if (boards.isEmpty()) {
				System.out.println("Nenhum story board cadastrado!");
				return;
			}

			System.out.println("Selecione o board para excluir:");
			boards.forEach(b -> System.out.println(b.getId() + ": " + b.getName()));

			System.out.print("Digite o ID ou 0 para cancelar: ");

			if (!scanner.hasNextLong()) {
				System.out.println("ID inválido!");
				scanner.nextLine();
				return;
			}

			Long id = scanner.nextLong();
			scanner.nextLine();

			if (id == 0) {
				System.out.println("Operação cancelada.");
				return;
			}

			if (boardRepository.existsById(id)) {
				boardRepository.deleteById(id);
				System.out.println("Story board excluído com sucesso!");
			} else {
				System.out.println("ID não encontrado!");
			}
		} catch (DataAccessException e) {
			System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erro ao excluir board: " + e.getMessage());
		}
	}

	private void boardManagementMenu(Long boardId) {
		try {
			StoryBoard board = boardRepository.findById(boardId)
					.orElseThrow(() -> new RuntimeException("Board não encontrado"));

			while (true) {
				System.out.println("\n=== Menu do Story Board: " + board.getName() + " ===");
				System.out.println("1. Criar novo segmento");
				System.out.println("2. Mover segmento");
				System.out.println("3. Bloquear/Desbloquear segmento");
				System.out.println("4. Finalizar story board");
				System.out.println("5. Relatório de Progresso");
				System.out.println("6. Relatório de Revisão");
				System.out.println("7. Voltar");

				if (!scanner.hasNextInt()) {
					System.out.println("Entrada inválida!");
					scanner.nextLine();
					continue;
				}

				int choice = scanner.nextInt();
				scanner.nextLine();

				if (board.isFinalized() && choice <= 4) {
					System.out.println("Board finalizado! Apenas relatórios disponíveis.");
					continue;
				}

				try {
					switch (choice) {
						case 1 -> createNewCard(boardId);
						case 2 -> moveCard(boardId);
						case 3 -> toggleCardBlock(boardId);
						case 4 -> {
							finalizeBoard(boardId);
							return;
						}
						case 5 -> generateProgressReport(boardId);
						case 6 -> generateReviewReport(boardId);
						case 7 -> { return; }
						default -> System.out.println("Opção inválida!");
					}
				} catch (Exception e) {
					System.out.println("Erro na operação: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao acessar o board: " + e.getMessage());
		}
	}

	private void createNewCard(Long boardId) {
		try {
			StoryBoard board = boardRepository.findById(boardId)
					.orElseThrow(() -> new RuntimeException("Board não encontrado"));

			List<BoardColumn> columns = boardColumnRepository.findByStoryBoardOrderByColumnOrderAsc(board);

			if (columns.isEmpty()) {
				System.out.println("Erro: Board sem colunas!");
				return;
			}

			BoardColumn introColumn = columns.get(0);

			if (!introColumn.getType().equals(ColumnType.INTRODUCTION)) {
				System.out.println("Erro: Configuração inválida do board!");
				return;
			}

			System.out.println("Título do segmento:");
			String title = scanner.nextLine();

			if (title == null || title.trim().isEmpty()) {
				System.out.println("Erro: Título não pode ser vazio!");
				return;
			}

			System.out.println("Descrição:");
			String description = scanner.nextLine();

			cardService.createCard(introColumn, title, description);
			System.out.println("Novo segmento criado na Introdução!");
		} catch (DataIntegrityViolationException e) {
			System.out.println("Erro: Dados inválidos para o card!");
		} catch (Exception e) {
			System.out.println("Erro ao criar card: " + e.getMessage());
		}
	}

	private void finalizeBoard(Long boardId) {
		try {
			StoryBoard board = boardRepository.findById(boardId)
					.orElseThrow(() -> new RuntimeException("Board não encontrado"));
			board.setFinalized(true);
			boardRepository.save(board);
			System.out.println("Story board finalizado!");
		} catch (Exception e) {
			System.out.println("Erro ao finalizar o board: " + e.getMessage());
		}
	}

	private void moveCard(Long boardId) {
		try {
			List<Card> cards = cardRepository.findByStoryBoardId(boardId);
			if (cards.isEmpty()) {
				System.out.println("Nenhum segmento disponível!");
				return;
			}

			System.out.println("Selecione o segmento:");
			cards.forEach(c -> System.out.println(c.getId() + " | " + c.getTitle() + (c.isBlocked() ? " (BLOQUEADO)" : "")));
			Long cardId = scanner.nextLong();
			scanner.nextLine();

			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new RuntimeException("Card não encontrado"));
			if (card.isBlocked()) {
				System.out.println("Segmento bloqueado! Desbloqueie primeiro.");
				return;
			}

			BoardColumn currentColumn = card.getColumn();
			List<BoardColumn> allColumns = boardColumnRepository.findByStoryBoardOrderByColumnOrderAsc(currentColumn.getStoryBoard());

			List<BoardColumn> availableColumns = allColumns.stream()
					.filter(c -> c.getColumnOrder() > currentColumn.getColumnOrder() || c.getType().equals(ColumnType.ALTERNATIVE))
					.collect(Collectors.toList());

			if (availableColumns.isEmpty()) {
				System.out.println("Não há colunas disponíveis para mover!");
				return;
			}

			System.out.println("Selecione a coluna de destino:");
			for (int i = 0; i < availableColumns.size(); i++) {
				BoardColumn col = availableColumns.get(i);
				System.out.println((i + 1) + ". " + col.getName() + " (" + col.getType() + ")");
			}

			int colChoice = scanner.nextInt();
			scanner.nextLine();
			if (colChoice < 1 || colChoice > availableColumns.size()) {
				System.out.println("Opção inválida!");
				return;
			}

			BoardColumn selectedColumn = availableColumns.get(colChoice - 1);
			cardService.moveCardToColumn(card, selectedColumn);
			System.out.println("Segmento movido para " + selectedColumn.getName() + "!");
		} catch (Exception e) {
			System.out.println("Erro ao mover o segmento: " + e.getMessage());
		}
	}

	private void toggleCardBlock(Long boardId) {
		try {
			List<Card> cards = cardRepository.findByStoryBoardId(boardId);
			if (cards.isEmpty()) {
				System.out.println("Nenhum segmento disponível!");
				return;
			}

			cards.forEach(c -> System.out.println(c.getId() + " | " + c.getTitle()));
			System.out.println("Selecione o segmento:");

			if (!scanner.hasNextLong()) {
				System.out.println("ID inválido!");
				scanner.nextLine();
				return;
			}

			Long cardId = scanner.nextLong();
			scanner.nextLine();

			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new RuntimeException("Card não encontrado"));

			System.out.println("Digite o motivo:");
			String reason = scanner.nextLine();

			if (reason == null || reason.trim().isEmpty()) {
				System.out.println("Motivo não pode ser vazio!");
				return;
			}

			boolean newStatus = !card.isBlocked();
			card.setBlocked(newStatus);
			cardRepository.save(card);

			blockedCardHistoryService.logBlockAction(card, reason, newStatus);
			System.out.println("Segmento " + (newStatus ? "bloqueado" : "desbloqueado") + " com sucesso!");
		} catch (DataAccessException e) {
			System.out.println("Erro de acesso aos dados: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erro ao bloquear/desbloquear: " + e.getMessage());
		}
	}

	private void generateProgressReport(Long boardId) {
		try {
			List<Card> cards = cardRepository.findByStoryBoardId(boardId);
			System.out.println("\n=== Relatório de Progresso ===");
			for (Card card : cards) {
				List<CardMovementHistory> movements = movementHistoryRepository.findByCardOrderByMovedAtAsc(card);
				System.out.println("\nSegmento: " + card.getTitle());
				for (int i = 0; i < movements.size(); i++) {
					CardMovementHistory move = movements.get(i);
					LocalDateTime start = move.getMovedAt();
					LocalDateTime end = (i < movements.size() - 1) ? movements.get(i + 1).getMovedAt() : LocalDateTime.now();

					long hours = Duration.between(start, end).toHours();
					System.out.printf("- %s → %s: %d horas%n",
							move.getFromColumn().getName(),
							move.getToColumn().getName(),
							hours);
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao gerar relatório de progresso: " + e.getMessage());
		}
	}

	private void generateReviewReport(Long boardId) {
		try {
			List<BlockedCardHistory> history = blockedCardHistoryRepository.findByStoryBoardId(boardId);
			System.out.println("\n=== Relatório de Revisão ===");
			history.forEach(h -> System.out.printf(
					"[%s] %s: %s (%s)%n",
					h.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
					h.getCard().getTitle(),
					h.getReason(),
					h.isBlockedStatus() ? "BLOQUEIO" : "DESBLOQUEIO"
			));
		} catch (Exception e) {
			System.out.println("Erro ao gerar relatório de revisão: " + e.getMessage());
		}
	}
}
