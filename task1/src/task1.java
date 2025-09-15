import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class task1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int message = myrank;
        int TAG = 0;
        Status status = null;

        // четные ранги отправляют сообщение
        if ((myrank % 2) == 0) {
            if ((myrank + 1) != size) {
                MPI.COMM_WORLD.Send(new int[]{message}, 0, 1, MPI.INT, myrank + 1, TAG);
            }
        }
        // нечетные ранги принимают сообщение
        else {
            if (myrank != 0) {
                int[] recvBuffer = new int[1];
                MPI.COMM_WORLD.Recv(recvBuffer, 0, 1, MPI.INT, myrank - 1, TAG);
                message = recvBuffer[0];
                System.out.println("Process " + myrank + " received: " + message);
            }
        }
        MPI.Finalize();
    }
}