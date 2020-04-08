package cn.sher6j.statement;

/**
 * 演示使用PreparedStatement替换Statement解决SQL注入问题
 * @author sher6j
 * @create 2020-04-07-17:13
 *
 * PreparedStatement解决SQL注入原理？
 * PreparedStatement是预编译，且的关系就是且的关系，不会因为SQL注入变成或的关系
 *
 * 除了解决Statement的拼串、SQL注入问题之外，PreparedStatement还有哪些好处？
 *  1. PreparedStatement操作Blog的数据，而Statement做不到
 *  2. PreparedStatement可以实现更高效的批量操作
 */
public class PreparedStatementTest {
}
