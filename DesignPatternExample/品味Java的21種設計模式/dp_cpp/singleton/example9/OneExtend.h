#pragma once

#include <string>
#include <map>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example9
				{
					///
					/// <summary> * ����ʾ�����չ����ģʽ������ʵ����ĿΪ3��  </summary>
					/// 
					class OneExtend
					{
					///	
					///	 <summary> * ����һ��ȱʡ��keyֵ��ǰ׺ </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string DEFAULT_PREKEY = "Cache";
					///	
					///	 <summary> * ����ʵ�������� </summary>
					///	 
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Map<std::string, OneExtend*> *map = std::map<std::string, OneExtend*>();
					///	
					///	 <summary> * ������¼��ǰ����ʹ�õڼ���ʵ�������˿��Ƶ������Ŀ���ͷ��ش�1��ʼ </summary>
					///	 
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static int num = 1;
					///	
					///	 <summary> * �������ʵ���������Ŀ </summary>
					///	 
						static const int NUM_MAX = 3;
						OneExtend();
					public:
						static OneExtend *getInstance();

						static void main(std::string args[]);
					};

				}
			}
		}
	}
}