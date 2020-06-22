package uzc.kit.entity.response;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.http.ATResponse;
import uzc.kit.service.impl.grpc.BrsApi;
import org.bouncycastle.util.encoders.Hex;

public class AT {
    private final boolean dead;
    private final boolean finished;
    private final boolean frozen;
    private final boolean running;
    private final boolean stopped;
    private final UzcAddress creator;
    private final UzcAddress id;
    private final UzcValue balance;
    private final UzcValue minimumActivation;
    private final UzcValue previousBalance;
    private final byte[] machineCode;
    private final byte[] machineData;
    private final int creationHeight;
    private final int nextBlockHeight;
    private final int version;
    private final String description;
    private final String name;

    public AT(boolean dead, boolean finished, boolean frozen, boolean running, boolean stopped, UzcAddress creator, UzcAddress id, UzcValue balance, UzcValue minimumActivation, UzcValue previousBalance, byte[] machineCode, byte[] machineData, int creationHeight, int nextBlockHeight, int version, String description, String name) {
        this.dead = dead;
        this.finished = finished;
        this.frozen = frozen;
        this.running = running;
        this.stopped = stopped;
        this.creator = creator;
        this.id = id;
        this.balance = balance;
        this.minimumActivation = minimumActivation;
        this.previousBalance = previousBalance;
        this.machineCode = machineCode;
        this.machineData = machineData;
        this.creationHeight = creationHeight;
        this.nextBlockHeight = nextBlockHeight;
        this.version = version;
        this.description = description;
        this.name = name;
    }

    public AT(ATResponse atResponse) {
        this.dead = atResponse.isDead();
        this.finished = atResponse.isFinished();
        this.frozen = atResponse.isFrozen();
        this.running = atResponse.isRunning();
        this.stopped = atResponse.isStopped();
        this.creator = UzcAddress.fromEither(atResponse.getCreator());
        this.id = UzcAddress.fromEither(atResponse.getAt());
        this.balance = UzcValue.fromPlanck(atResponse.getBalanceNQT());
        this.minimumActivation = UzcValue.fromPlanck(atResponse.getMinActivation());
        this.previousBalance = UzcValue.fromPlanck(atResponse.getPrevBalanceNQT());
        this.machineCode = Hex.decode(atResponse.getMachineCode());
        this.machineData = Hex.decode(atResponse.getMachineData());
        this.creationHeight = atResponse.getCreationBlock();
        this.nextBlockHeight = atResponse.getNextBlock();
        this.version = atResponse.getAtVersion();
        this.description = atResponse.getDescription();
        this.name = atResponse.getName();
    }

    public AT(BrsApi.AT at) {
        this.dead = at.getDead();
        this.finished = at.getFinished();
        this.frozen = at.getFrozen();
        this.running = at.getRunning();
        this.stopped = at.getStopped();
        this.creator = UzcAddress.fromId(at.getCreator());
        this.id = UzcAddress.fromId(at.getId());
        this.balance = UzcValue.fromPlanck(at.getBalance());
        this.minimumActivation = UzcValue.fromPlanck(at.getMinActivation());
        this.previousBalance = UzcValue.fromPlanck(at.getPreviousBalance());
        this.machineCode = at.getMachineCode().toByteArray();
        this.machineData = at.getMachineData().toByteArray();
        this.creationHeight = at.getCreationBlock();
        this.nextBlockHeight = at.getNextBlock();
        this.version = at.getVersion();
        this.description = at.getDescription();
        this.name = at.getName();
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public UzcAddress getCreator() {
        return creator;
    }

    public UzcAddress getId() {
        return id;
    }

    public UzcValue getBalance() {
        return balance;
    }

    public UzcValue getMinimumActivation() {
        return minimumActivation;
    }

    public UzcValue getPreviousBalance() {
        return previousBalance;
    }

    public byte[] getMachineCode() {
        return machineCode;
    }

    public byte[] getMachineData() {
        return machineData;
    }

    public int getCreationHeight() {
        return creationHeight;
    }

    public int getNextBlockHeight() {
        return nextBlockHeight;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
