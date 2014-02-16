package ReactiveAPI.Security

trait Nameable {
  val name : String
  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case nameable: Nameable => this.==(nameable)
      case other => super.equals(other)
    }
  }
  def ==(n: Nameable) : Boolean = this.name == n.name
  def !=(n: Nameable) : Boolean = !this.==(n)
}
object Rights {
  implicit class StringHelper(val sc: StringContext) extends AnyVal {
    def permission(args: Any*) = Permission(sc.s())
    def role(args: Any*) = Role(sc.s())
  }
}
trait Permission extends Nameable
object Permission {
  def apply(n: String) = new Permission { override val name = n }
}
trait Role extends Nameable
object Role {
  def apply(n: String) = new Role { override val name = n }
}