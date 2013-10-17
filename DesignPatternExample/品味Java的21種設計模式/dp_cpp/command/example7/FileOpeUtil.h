#pragma once

#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example7
				{

					///
					/// <summary> * ��д�ļ��ĸ��������� </summary>
					/// 
					class FileOpeUtil
					{
					///	
					///	 <summary> * ˽�л����췽���������ⲿ��ν�Ĵ�����ʵ��
					///	 * ��������಻��Ҫ������ʵ�� </summary>
					///	 
					private:
						FileOpeUtil();
					///	
					///	 <summary> * ���ļ������ļ������ȡ�洢��List���� </summary>
					///	 * <param name="pathName"> �ļ�·�����ļ��� </param>
					///	 * <returns> �洢��List���� </returns>
					///	 
					public:
						static std::vector readFile(std::string pathName);
					///	
					///	 <summary> * д�ļ�����list����д�����ļ���ȥ </summary>
					///	 * <param name="pathName"> �ļ�·�����ļ��� </param>
					///	 * <param name="list"> Ҫд���ļ���list���� </param>
					///	 
						static void writeFile(std::string pathName, std::vector list);
					};

				}
			}
		}
	}
}