import manager.Managers;
import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    /*@BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }*/
@Override
    public InMemoryTaskManager createManager() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        return manager;
    }

}