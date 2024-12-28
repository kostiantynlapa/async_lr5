import java.util.concurrent.*;

public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Завдання 1: Об'єднання результатів двох асинхронних задач
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            sleep(1000); // робота (процес)
            return "Результат 1";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(1200); // робота
            return "Результат 2";
        });

        CompletableFuture<String> combinedResult = task1.thenCombine(task2, (result1, result2) -> {
            return result1 + " і " + result2;
        });

        System.out.println("Об'єднаний результат: " + combinedResult.get());

        // Завдання 2: Вибір найкращого програмного забезпечення

        CompletableFuture<Integer> priceFuture = CompletableFuture.supplyAsync(() -> {
            sleep(800); //  отримання ціни
            return 100;
        });

        CompletableFuture<Integer> functionalityFuture = CompletableFuture.supplyAsync(() -> {
            sleep(700); // оцінка функціональності
            return 80;
        });

        CompletableFuture<Integer> supportFuture = CompletableFuture.supplyAsync(() -> {
            sleep(900); // аналіз підтримки
            return 90;
        });

        CompletableFuture<Void> allCriteria = CompletableFuture.allOf(priceFuture, functionalityFuture, supportFuture);

        CompletableFuture<String> bestOption = allCriteria.thenApply(v -> {
            int price = priceFuture.join();
            int functionality = functionalityFuture.join();
            int support = supportFuture.join();

            int totalScore = functionality + support - price / 10;
            return "Кращий варіант з рейтингом: " + totalScore;
        });

        System.out.println(bestOption.get());

        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(priceFuture, functionalityFuture, supportFuture);
        System.out.println("Перше виконане завдання дало результат: " + firstCompleted.get());

    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
