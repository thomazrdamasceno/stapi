package com.siertech.stapi.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.siertech.stapi.crud.CrudClass;
import com.siertech.stapi.crud.CrudClassUtil;
import com.siertech.stapi.database.DataBaseUtil;
import com.siertech.stapi.security.AccountUserDetails;
import com.siertech.stapi.system.SystemUtil;
import com.siertech.stapi.util.DataUtil;

@Repository
public abstract class GenericDAO<E> {

	Class<E> classe;

	@Autowired
	SessionFactory sessionFactory;

	public Class<E> getClasse() {

		return classe;
	}


	public void setClasse(Class<E> classe) {
		this.classe = classe;
	}


	public GenericDAO(Class<E> classe){

		this.classe  = classe;

	}

	public SessionFactory getSessionFactory() {


		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public E merge(E item) {

		getSessionFactory().getCurrentSession().merge(item);
		return item;
	}


	//queryValue = ex: nome='Thomaz'
	public int changeAttr(long id, String key, String value) {

		String queryUpdate  = "update  "+classe.getSimpleName()+" set "+key+"="+value+" where id = :id";
		Query query= getSessionFactory().getCurrentSession().createQuery(queryUpdate);
		query.setLong("id", id);

		return query.executeUpdate();

	}


	public E update(E item) {


		System.out.println("Chamou o Generico");
		getSessionFactory().getCurrentSession().update(item);
		return item;
	}


	public E addOrUpdate(E item) {

		if(isCrudEntity()){

			CrudClass ent  = (CrudClass) item;

			long idOperador = 0;

			AccountUserDetails user = SystemUtil.getCurrentUserDetails();

			if(user!=null){

				idOperador = user.getAccount().getId();

				//Operador que executou a ação
				if(ent.getId()==0){
				ent.setIdOperador(idOperador);
				}


				String descricaoOperacao = CrudClassUtil.getDescricaoOperacaoOperador(ent,user.getAccount().getNome());

				//Historico de manipulações de operador 
				if(ent.getHistoricoObjeto()==null)
					ent.setHistoricoObjeto(descricaoOperacao);

				else if(ent.getHistoricoObjeto().length()<500)
					ent.setHistoricoObjeto(ent.getHistoricoObjeto()+","+descricaoOperacao);


				//filial relacionada
				if(ent.getId()==0 && ent.getIdFilial()==0){
					ent.setIdFilial(user.getAccount().getCurrentFilialId());
				}
			}



			if(ent.getId()==0 && ent.getDataCadastro()==null)
				ent.setDataCadastro(DataUtil.getFormated(new Date()));

			getSessionFactory().getCurrentSession().saveOrUpdate(ent);
			return item;
		}

		getSessionFactory().getCurrentSession().saveOrUpdate(item);
		return item;
	}


	public E addOrUpdateAll(ArrayList<E> itens) {


		for(E item :itens)
			getSessionFactory().getCurrentSession().saveOrUpdate(item);


		return null;
	}



	@SuppressWarnings("unchecked")
	public E getById(long id) {

		return (E) getSessionFactory().getCurrentSession().createCriteria(classe).add(Restrictions.eq("id",id)).uniqueResult();

	}

	@SuppressWarnings("unchecked")
	public ArrayList<E> getByIds(long[] ids) {

		ArrayList<E> lista = new ArrayList<E>();
		E item;
		for(long id : ids){

			item = (E) getSessionFactory().getCurrentSession().createCriteria(classe).add(Restrictions.eq("id",id)).uniqueResult();
			lista.add(item);
		}

		return lista;

	}


	@SuppressWarnings("unchecked")
	public E getByAttr(String attr, Object valor) {

		return (E) getSessionFactory().getCurrentSession().createCriteria(classe).add(Restrictions.eq(attr,valor)).uniqueResult();

	}


	public void remove(E item) {

		getSessionFactory().getCurrentSession().delete(item);
	}


	@SuppressWarnings("unchecked")
	public ArrayList<E> getAll() {

		String queryCrud= DataBaseUtil.getDisableQuery(classe);
		
		if(queryCrud.length()>0)
			queryCrud  = " where "+queryCrud;
        
		System.out.println("disableQuery: "+queryCrud);

		ArrayList<E>  lista = (ArrayList<E>) getSessionFactory().getCurrentSession().createQuery("from "+classe.getSimpleName()+queryCrud).list();
		ArrayList<E>  aux = new ArrayList<E>();

		for(int i=lista.size()-1;i>=0;i--)
			aux.add(lista.get(i));
		return aux;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<E> getLike(String propriedade,String query) {

		String queryCrud = DataBaseUtil.getInlineCrudQueries(classe);

		queryCrud = " and "+queryCrud;
         
		ArrayList<E> itens;
		String queryParaExecutar = "from "+classe.getSimpleName()+" where "+propriedade+" like :query"+queryCrud;
		System.out.println("Query a ser executada no getLike: "+queryParaExecutar);
		Query q = getSessionFactory().getCurrentSession().createQuery(queryParaExecutar);
		q.setString("query","%"+query+"%");
		itens = (ArrayList<E>) q.list();
		return itens;

	}


	//Realiza a montagem da query para o padrão do banco de dados
	public String montaQuery(String[] qs,String extra,String queryColumns){

		//É informado queryColumns quando eu precico recuperar colunas específicas do objeto ex: select count(*), max(idade), etc
		//Quando não informado, o objeto inteiro é recuperado
		//Obs: a palavra chave select deve ser informada quando necessário reuperar colunas específicas
		if(queryColumns==null)
			queryColumns="";

		qs = DataBaseUtil.getCrudQueries(this.classe, qs);

		if(!extra.contains("order by"))
			extra+=" order by id DESC";

		String condicao="";
		String query = queryColumns+ " from "+classe.getSimpleName()+"";
		int i=0;

		for(String q :qs){

			if(q.length()==0 || q==null)
				continue;

			condicao+=" "+q+" ";
			i++;

			if(i<qs.length)
				condicao+=" and ";

		}

		if(condicao.length()>0){

			query+=" where ";
			query+=condicao;
		}

		query+=" "+extra;

	
		System.out.println("query no GenericDao.montaQuery(): "+query);

		return query;

	}

	//Mapa de Valores
	@SuppressWarnings("unchecked")
	public ArrayList<E> getLikeMap(String[] qs, int pagina, int max, String extra) {

		//Realiza a montagem da query
		String query =  montaQuery(qs,extra,"");
		System.out.println("Query a ser executada: "+query);
		ArrayList<E> itens;
		Query q = getSessionFactory().getCurrentSession().createQuery(query);

		q.setFirstResult(pagina * max);
		q.setMaxResults(max);
		itens = (ArrayList<E>) q.list();
		return itens;

	}

	//Retorna um Long com o total de itens de acordo com as querys especificadas em 'qs' e 'extra'
	public Long getCountItens(String[] qs,String extra){

		//Realiza a montagem da query
		String query =  montaQuery(qs,extra,"select count(*)");
		System.out.println("query em getCountItens: "+query);
		return (Long)  getSessionFactory().getCurrentSession().createQuery(query).uniqueResult();
	
	}

	public void deleteByIds(long[] ids) {


		String query ="";

		if(isCrudEntity()){

			query="update from "+classe.getSimpleName()+" set disable=1 where id=:id";
		}
		else{
			query = "delete from "+classe.getSimpleName()+" where id=:id";
		}


		Query q = getSessionFactory().getCurrentSession().createQuery(query);
		System.out.println("Query executada: "+q.getQueryString());
		for(int i=0;i<ids.length;i++){
			q.setLong("id",ids[i]);
			q.executeUpdate();
		}


	}

	public void deleteAll(){

		Query q = getSessionFactory().getCurrentSession().createSQLQuery("delete from "+classe.getSimpleName());
		q.executeUpdate();


	}

	public Object executeFunction(String objeto,String function,String column,String extra){

		function = function+"("+column+")";

		Query q = getSessionFactory().getCurrentSession().createQuery("select "+function+" from "+objeto+" where "+extra);


		return  (Object) q.uniqueResult();

	}

	public boolean isCrudEntity(){

		return DataBaseUtil.isCrudEntity(classe);
	}


	@SuppressWarnings("unchecked")
	public ArrayList<Map<String,Object>> getProjecoes (String[] qs,String extra,String columns, String groupBy){

		String query = "select "+columns+" from "+classe.getSimpleName()+" where ";
		String condicao ="";
		int i=0;
		for(String q :qs){

			if(q.length()==0 || q==null)
				continue;

			condicao+=" "+q+" ";
			i++;

			if(i<qs.length)
				condicao+=" and ";

		}

		query+=condicao;

		if(extra!=null && extra.length()>0){
			if(qs.length>0){
				extra = " and "+extra;
			}
			query+=extra;
		}

		System.out.println("groupBy: "+groupBy);
		if(!groupBy.equals("null"))
			query+=" group by "+groupBy;


		System.out.println("Query a ser executada: "+query);

		Query q = this.getSessionFactory().getCurrentSession().createQuery(query);
		return (ArrayList<Map<String,Object>>) q.list();

	}





}
