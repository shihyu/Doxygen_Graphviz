#pragma once

#include "DepUserModel.h"
#include "Dep.h"
#include "User.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example3
				{

					///
					/// <summary> * ʵ�ֲ��ź���Ա�������н���ʵ����
					/// * ˵����Ϊ����ʾ�ļ���ԣ�ֻʾ��ʵ�ֳ������ź���Ա��ְ�Ĺ��� </summary>
					/// 
					class DepUserMediatorImpl
					{
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static DepUserMediatorImpl *mediator = new DepUserMediatorImpl();
						DepUserMediatorImpl();
					public:
						static DepUserMediatorImpl *getInstance();

					///	
					///	 <summary> * �����ã���¼���ź���Ա�Ĺ�ϵ </summary>
					///	 
					private:
						Collection<DepUserModel*> *depUserCol;
					///	
					///	 <summary> * ��ʼ���������� </summary>
					///	 
						void initTestData();
					///	
					///	 <summary> * ����������ŵĲ��������������Ա�Ľ�������Ҫȥ����Ӧ�Ĺ�ϵ </summary>
					///	 * <param name="depId"> �������Ĳ��Ŷ���ı�� </param>
					///	 * <returns> �Ƿ��Ѿ���ȷ�Ĵ������������������������Ա�Ľ��� </returns>
					///	 
					public:
						virtual bool deleteDep(std::string depId);
					///	
					///	 <summary> * �������Ա��ְ������벿�ŵĽ��� </summary>
					///	 * <param name="userId"> ��ְ����Ա�ı�� </param>
					///	 * <returns> �Ƿ���ȷ����������Ա��ְ������벿�ŵĽ��� </returns>
					///	 
						virtual bool deleteUser(std::string userId);
					///	
					///	 <summary> * �����ã����ڲ���ӡ��ʾһ��һ�������µ�������Ա </summary>
					///	 * <param name="dep"> ���Ŷ��� </param>
					///	 
						virtual void showDepUsers(Dep *dep);
					///	
					///	 <summary> * �����ã����ڲ���ӡ��ʾһ��һ����Ա�����Ĳ��� </summary>
					///	 * <param name="user"> ��Ա���� </param>
					///	 
						virtual void showUserDeps(User *user);
					///	
					///	 <summary> * �������Ա��������������벿�ŵĽ��� </summary>
					///	 * <param name="userId"> ����������Ա�ı�� </param>
					///	 * <param name="oldDepId"> ����ǰ�Ĳ��ŵı�� </param>
					///	 * <param name="newDepId"> ������Ĳ��ŵı�� </param>
					///	 * <returns> �Ƿ���ȷ����������Ա��������������벿�ŵĽ��� </returns>
					///	 
						virtual bool changeDep(std::string userId, std::string oldDepId, std::string newDepId);


					///	
					///	 <summary> * ������źϲ����������������Ա�Ľ��� </summary>
					///	 * <param name="colDepIds"> ��Ҫ�ϲ��Ĳ��ŵı�ż��� </param>
					///	 * <param name="newDep"> �ϲ����µĲ��Ŷ��� </param>
					///	 * <returns> �Ƿ���ȷ���������źϲ����������������Ա�Ľ��� </returns>
					///	 
						virtual bool joinDep(Collection<std::string> *colDepIds, Dep *newDep);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								depUserCol = std::vector<DepUserModel*>();

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}