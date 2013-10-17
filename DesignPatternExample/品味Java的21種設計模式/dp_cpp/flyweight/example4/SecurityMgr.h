#pragma once

#include "Flyweight.h"
#include "TestDB.h"
#include "UnsharedConcreteFlyweight.h"
#include "FlyweightFactory.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example4
				{
					///
					/// <summary> * ��ȫ����ʵ�ֳɵ��� </summary>
					/// 
					class SecurityMgr
					{
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static SecurityMgr *securityMgr = new SecurityMgr();
						SecurityMgr();
					public:
						static SecurityMgr *getInstance();

					///	
					///	 <summary> * �������ڼ䣬������ŵ�¼��Ա��Ӧ��Ȩ�ޣ�
					///	 * ��WebӦ���У���Щ����ͨ�����ŵ�session�� </summary>
					///	 
					private:
						Map<std::string, Collection<Flyweight*>*> *map;

					///	
					///	 <summary> * ģ���¼�Ĺ��� </summary>
					///	 * <param name="user"> ��¼���û� </param>
					///	 
					public:
						virtual void login(std::string user);
					///	
					///	 <summary> * �ж�ĳ�û���ĳ����ȫʵ���Ƿ�ӵ��ĳȨ�� </summary>
					///	 * <param name="user"> �����Ȩ�޵��û�  </param>
					///	 * <param name="securityEntity"> ��ȫʵ�� </param>
					///	 * <param name="permit"> Ȩ�� </param>
					///	 * <returns> true��ʾӵ����ӦȨ�ޣ�false��ʾû����ӦȨ�� </returns>
					///	 
						virtual bool hasPermit(std::string user, std::string securityEntity, std::string permit);
					///	
					///	 <summary> * �����ݿ��л�ȡĳ����ӵ�е�Ȩ�� </summary>
					///	 * <param name="user"> ��Ҫ��ȡ��ӵ�е�Ȩ�޵���Ա </param>
					///	 * <returns> ĳ����ӵ�е�Ȩ�� </returns>
					///	 
					private:
						Collection<Flyweight*> *queryByUser(std::string user);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								map = std::map<std::string, Collection<Flyweight*>*>();

								initialized = true;
							}
						}
					};


				}
			}
		}
	}
}