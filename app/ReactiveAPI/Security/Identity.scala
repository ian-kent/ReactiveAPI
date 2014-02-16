package ReactiveAPI.Security

import _root_.play.api.mvc.SimpleResult
import scala.concurrent._
import ExecutionContext.Implicits.global

object Identity {
  def apply() : Identity[Permission, Role] = apply()
  def apply(r: Seq[Role] = Seq(), p: Seq[Permission] = Seq()) = new Identity[Permission, Role]{
    override val roles: Seq[Role] = r
    override val permissions: Seq[Permission] = p
  }
}
case class WrappedIdentity[P>:Permission, R>:Role](identity: Option[Identity[P,R]]) extends Identity[P,R] {
  val i = identity.getOrElse(null)

  override val roles: Seq[R] = i.roles
  override val permissions: Seq[P] = i.permissions


  override def hasEither(condition: => Either[P, R])(ifBlock: => Future[SimpleResult]) = i.hasEither(condition)(ifBlock)
  override def %|(permission: P): Future[Boolean] = i.%|(permission)
  override def %|(role: => R): Future[Boolean] = i.%|(role)
}
trait Identity[P>:Permission, R>:Role] {
  val permissions : Seq[P] = Seq()
  val roles : Seq[R] = Seq()

  def %|(permission: P) : Future[Boolean] = future { permissions contains permission }
  def %|(role: => R) : Future[Boolean] = future { roles contains role }
  def %|(roleOrPermission: Either[P,R]) : Future[Boolean] = roleOrPermission match {
    case Left(p) => %|(p)
    case Right(r) => %|(r)
  }

  def has(condition: P) = hasEither(Left(condition)) _
  def has(condition: => R) = hasEither(Right(condition)) _
  def hasEither(condition: => Either[P,R])(ifBlock: => Future[SimpleResult]) = new {
    def otherwise(otherwiseBlock: => Future[SimpleResult]) = %|(condition) flatMap {
      case true => ifBlock
      case _ => otherwiseBlock
    }
  }
}