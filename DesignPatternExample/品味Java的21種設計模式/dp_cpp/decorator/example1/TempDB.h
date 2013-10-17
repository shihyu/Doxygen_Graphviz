#pragma once

#include <string>
#include <map>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example1
				{
					///
					/// <summary> * ���ڴ���ģ�����ݿ⣬׼����������ݣ��ü��㽱�� </summary>
					/// 
					class TempDB
					{
					private:
						TempDB();
					///	
					///	 <summary> * ��¼ÿ���˵��¶����۶ֻ������Ա���·�û���� </summary>
					///	 
					public:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Map<std::string, double> *mapMonthSaleMoney = std::map<std::string, double>();

//JAVA TO C++ CONVERTER TODO TASK: Static constructors are not allowed in native C++:
						static TempDB();
					};

				}
			}
		}
	}
}