#pragma once

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
				namespace example1
				{
					///
					/// <summary> * �������ã����ڴ���ģ�����ݿ��е�ֵ </summary>
					/// 
					class TestDB
					{
					///	
					///	 <summary> * ���������Ȩ���ݵ�ֵ </summary>
					///	 
					public:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Collection<std::string> *colDB = std::vector<std::string>();

//JAVA TO C++ CONVERTER TODO TASK: Static constructors are not allowed in native C++:
						static TestDB();
					};

				}
			}
		}
	}
}