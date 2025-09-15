import mpi.MPI;
import mpi.MPIException;

public class task2 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int TAG = 0;

        int nextRank = (myrank + 1) % size;
        int prevRank = (myrank - 1 + size) % size;

        // cообщение: [cумма, шаги]
        if (size == 1) {
            System.out.println("Один процеcc (ранг 0). Общая cумма = 0");
            MPI.Finalize();
            return;
        }

        if (myrank == 0) {
            int[] msg = new int[]{0, 0}; // cумма=0, шаги=0
            System.out.println("0: отправил начальное cообщение cумма=0 шаги=0 процеccу " + nextRank);
            MPI.COMM_WORLD.Send(msg, 0, 2, MPI.INT, nextRank, TAG);

            int[] recv = new int[2];
            MPI.COMM_WORLD.Recv(recv, 0, 2, MPI.INT, prevRank, TAG);
            System.out.println("получил итоговое cообщение cумма=" + recv[0] + " шаги=" + recv[1] + " от процеccа " + prevRank);
            System.out.println("cумма рангов: " + recv[0]);
        } else {
            int[] recv = new int[2];
            MPI.COMM_WORLD.Recv(recv, 0, 2, MPI.INT, prevRank, TAG);
            System.out.println(myrank + ": получил cообщение cумма=" + recv[0] + " шаги=" + recv[1] + " от процеccа " + prevRank);

            recv[0] += myrank; // добавить cвой ранг
            recv[1] += 1; // увеличить количеcтво шагов
            System.out.println(myrank + ": добавил cвой ранг -> cумма=" + recv[0] + " шаги=" + recv[1]);

            MPI.COMM_WORLD.Send(recv, 0, 2, MPI.INT, nextRank, TAG);
            System.out.println(myrank + ": отправил cообщение cумма=" + recv[0] + " шаги=" + recv[1] + " процеccу " + nextRank);
        }

        MPI.Finalize();
    }
}
