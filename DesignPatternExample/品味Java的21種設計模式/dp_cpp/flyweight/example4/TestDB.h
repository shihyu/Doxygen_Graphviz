#pragma once

#include <string>
#include <map>
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
					/// <summary> * �������ã����ڴ���ģ�����ݿ��е�ֵ </summary>
					/// 
					class TestDB
					{
					///	
					///	 <summary> * ������ŵ�����Ȩ���ݵ�ֵ </summary>
					///	 
					public:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Collection<std::string> *colDB = std::vector<std::string>();
					///	
					///	 <summary> * ������������Ȩ���ݵ�ֵ��keyΪ������ݵ�id��valueΪ����ϰ����Ķ�����Ȩ���ݵ�ֵ </summary>
					///	 
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Map<std::string, std::string[]> *mapDB = std::map<std::string, std::string[]>();

//JAVA TO C++ CONVERTER TODO TASK: Static constructors are not allowed in native C++:
						static TestDB();
					};

				}
			}
		}
	}
}